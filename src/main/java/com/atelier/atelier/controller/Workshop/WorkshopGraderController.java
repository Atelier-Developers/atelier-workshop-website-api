package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.FormAnswerContext;
import com.atelier.atelier.context.GroupUsersContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.UserPortalManagment.Attender;
import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.GraderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FileAnswerRepository;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshopGrader")
public class WorkshopGraderController {

    private OfferingWorkshopRepository offeringWorkshopRepository;
    private UserRepository userRepository;
    private FormRepository formRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;
    private QuestionRepsoitory questionRepsoitory;
    private AnswerRepository answerRepository;
    private FileAnswerRepository fileAnswerRepository;

    public WorkshopGraderController(FileAnswerRepository fileAnswerRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository) {
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
        this.fileAnswerRepository = fileAnswerRepository;
    }

    @PostMapping("/offeringWorkshop/{id}/workshopForm/answer")
    public ResponseEntity<Object> answerToWorkshopForm(@PathVariable long id, Authentication authentication, @RequestBody FormAnswerContext formAnswerContext, @RequestParam(value = "file", required =  false) MultipartFile multipartFile) throws IOException {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);
        WorkshopGraderInfo workshopGraderInfo = graderWorkshopConnection.getWorkshopGraderInfoOfferedWorkshop(offeredWorkshop);
        if (workshopGraderInfo == null) {
            return new ResponseEntity<>("You aren't a grader of this workshop", HttpStatus.FORBIDDEN);
        }

        Optional<Form> optionalForm = formRepository.findById(formAnswerContext.getFormId());
        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Form form = optionalForm.get();
        Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(formAnswerContext.getApplicantId());
        if (!optionalWorkshopAttenderInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();
        if (workshopAttenderInfo.getOfferedWorkshop().getId() == offeredWorkshop.getId()) {
            if(workshopGraderInfo.getWorkshopGroup().getId() != workshopAttenderInfo.getWorkshopGroup().getId()){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            WorkshopAttenderFormApplicant workshopAttenderFormApplicant = new WorkshopAttenderFormApplicant();
            WorkshopGraderFormFiller workshopGraderFormFiller = new WorkshopGraderFormFiller();
            workshopGraderFormFiller.setWorkshopGraderInfo(workshopGraderInfo);
            workshopAttenderFormApplicant.setWorkshopAttenderInfo(workshopAttenderInfo);

            List<Answer> answers = new ArrayList<>();

            for (AnswerQuestionContext answerQuestionContext : formAnswerContext.getAnswerQuestionContexts()) {
                Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());
                if (!optionalQuestion.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                Question question = optionalQuestion.get();
                FilledAnswer filledAnswer = new FilledAnswer();
                if (question.getForm().getId() != form.getId()) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                String type = answerQuestionContext.getType();
                LinkedHashMap<String, Object> answerDataObject = answerQuestionContext.getAnswerData();
                AnswerData answerData = null;

                if (type.equalsIgnoreCase("TextAnswer")) {
                    TextAnswer textAnswer = new TextAnswer();
                    textAnswer.setText((String) answerDataObject.get("text"));
                    answerData = textAnswer;
                    System.out.println("here");
                } else if (type.equalsIgnoreCase("ChoiceAnswer")) {
                    ChoiceAnswer choiceAnswer = new ChoiceAnswer();
                    choiceAnswer.setChoice((Integer) answerDataObject.get("choice"));
                    answerData = choiceAnswer;
                } else if (type.equalsIgnoreCase("FileAnswer")){

                    FileAnswer fileAnswer = new FileAnswer();
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    fileAnswer.setFileName(fileName);
                    fileAnswer.setFileType(multipartFile.getContentType());
                    fileAnswer.setData(multipartFile.getBytes());

                    answerData = fileAnswer;

//                    // TODO ADDED FILE STUFF HERE
//                    fileAnswerRepository.save(fileAnswer);
                }
                else {
                    return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
                }


                filledAnswer.addAnswerData(answerData);

                filledAnswer.setFormFiller(workshopGraderFormFiller);
                workshopGraderFormFiller.addAnswer(filledAnswer);

                filledAnswer.setFormApplicant(workshopAttenderFormApplicant);
                workshopAttenderFormApplicant.addAnswers(filledAnswer);

                question.addAnswer(filledAnswer);
                filledAnswer.setQuestion(question);
                answers.add(filledAnswer);
            }
            for(Answer answer : answers){
                answerRepository.save(answer);

            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

    }


    // Get the grader and attendee users of the same group as this grader in an offering workshop
    @GetMapping("/offeringWorkshop/{id}/groupDetails")
    public ResponseEntity<Object> showUsersOfTheSameGroupAsThisGraderInAnOfferingWorkshop(@PathVariable long id, Authentication authentication){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);
        WorkshopGraderInfo workshopGraderInfo = graderWorkshopConnection.getWorkshopGraderInfoOfferedWorkshop(offeredWorkshop);
        if (workshopGraderInfo == null) {
            return new ResponseEntity<>("You aren't a grader of this workshop", HttpStatus.FORBIDDEN);
        }

        WorkshopGroup graderGroup = workshopGraderInfo.getWorkshopGroup();

        GroupUsersContext groupUsersContext = new GroupUsersContext();

        groupUsersContext.setGroupName(graderGroup.getName());
        groupUsersContext.setGroupId(graderGroup.getId());

        List<User> users = userRepository.findAll();

        List<User> attendeeUsers = new ArrayList<User>();

        for (WorkshopAttenderInfo workshopAttenderInfo : graderGroup.getAttenderInfos()){
            for (User user : users ){
                Attender attender = (Attender) user.getRole("Attender");
                if (attender.getAttenderWorkshopConnection().getId() == workshopAttenderInfo.getWorkshopAttender().getId()){
                    attendeeUsers.add(user);
                    break;
                }
            }
        }

        groupUsersContext.setAttendees(attendeeUsers);

        List<User> graderUsers = new ArrayList<User>();
        for (WorkshopGraderInfo workshopGraderInfo1 : graderGroup.getGraderInfos()){
            for(User user : users){
                Grader grader = (Grader) user.getRole("Grader");
                if (grader.getGraderWorkshopConnection().getId() == workshopGraderInfo1.getWorkshopGrader().getId()){
                    graderUsers.add(user);
                    break;
                }
            }
        }

        groupUsersContext.setGraders(graderUsers);

        return new ResponseEntity<>(groupUsersContext, HttpStatus.OK);
    }

    private GraderWorkshopConnection getGraderWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Grader grader = (Grader) user.getRole("Grader");
        return grader.getGraderWorkshopConnection();
    }

}
