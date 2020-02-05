package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.OfferedWorkshopManagerNameContext;
import com.atelier.atelier.context.RegisterRequestContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.*;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.role.GraderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopGraderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopGraderRepository;
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
@RequestMapping("/graders")
public class GraderController {

    private UserRepository userRepository;
    private GraderRepository graderRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private WorkshopGraderInfoRepository workshopGraderInfoRepository;
    private FormRepository formRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;
    private QuestionRepsoitory questionRepsoitory;
    private AnswerRepository answerRepository;
    private RequestRepository requestRepository;
    private WorkshopGraderRepository workshopGraderRepository;
    private FileAnswerRepository fileAnswerRepository;
    private FormApplicantRepository formApplicantRepository;

    public GraderController(FormApplicantRepository formApplicantRepository, WorkshopGraderRepository workshopGraderRepository, FileAnswerRepository fileAnswerRepository, UserRepository userRepository, GraderRepository graderRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopGraderInfoRepository workshopGraderInfoRepository, FormRepository formRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.formApplicantRepository = formApplicantRepository;
        this.workshopGraderRepository = workshopGraderRepository;
        this.graderRepository = graderRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
        this.formRepository = formRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
        this.requestRepository = requestRepository;
        this.fileAnswerRepository = fileAnswerRepository;
    }


    @GetMapping("/grader")
    public ResponseEntity<Object> getGraderRole(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Grader");
        if (role == null) {
            return new ResponseEntity<>("The user has no grader role.", HttpStatus.NO_CONTENT);
        }
        Grader graderRole = (Grader) role;
        return new ResponseEntity<>(graderRole, HttpStatus.OK);
    }

    // Returns OfferedWorkshopManagerNameContext objects of the workshops
    @GetMapping("/grader/workshops")
    public ResponseEntity<Object> getGraderWorkshops(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Grader");
        if (role == null) {
            return new ResponseEntity<>("This user has no grader role.", HttpStatus.NO_CONTENT);
        }
        Grader graderRole = (Grader) role;
        GraderWorkshopConnection graderWorkshopConnection = graderRole.getGraderWorkshopConnection();
        if (graderWorkshopConnection == null) {
            return new ResponseEntity<>("This user has not grader workshop connection.", HttpStatus.NO_CONTENT);
        }
        List<WorkshopGraderInfo> workshopGraderInfos = graderWorkshopConnection.getWorkshopGraderInfos();
        if (workshopGraderInfos.isEmpty()) {
            return new ResponseEntity<>("The user has no workshop grader info.", HttpStatus.NO_CONTENT);
        }
        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();
        List<User> users = userRepository.findAll();

        for (WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            OfferedWorkshop offeredWorkshop = workshopGraderInfo.getOfferedWorkshop();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            List<String> managerNames = new ArrayList<>();

            for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {
                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                for (User currentUser : users) {

                    WorkshopManager workshopManager1 = (WorkshopManager) currentUser.getRole("ManagerWorkshopConnection");

                    if (workshopManager.getId() == workshopManager1.getId()) {
                        managerNames.add(currentUser.getName());
                        break;
                    }
                }
            }

            offeredWorkshopManagerNameContext.setWorkshopManagers(managerNames);

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);

        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
    }


    private GraderWorkshopConnection getGraderWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Grader grader = (Grader) user.getRole("Grader");
        return grader.getGraderWorkshopConnection();
    }


    @GetMapping("/grader/request/offeringWorkshop/{id}")
    public ResponseEntity<Object> showGraderRequestForm(@PathVariable long id) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getGraderRequestForm(), HttpStatus.OK);
    }


    @PostMapping("/grader/request/offeringWorkshop/{id}/answer")
    public ResponseEntity<Object> answerGraderRequestForm(@PathVariable long id, Authentication authentication, @RequestBody RegisterRequestContext registerRequestContext, @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {


        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (!offeredWorkshop.hasGraderRequested(graderWorkshopConnection)) {
            return new ResponseEntity<>("The grader is already in this workshop", HttpStatus.FORBIDDEN);
        }
        for (WorkshopGraderInfo workshopGraderInfo : graderWorkshopConnection.getWorkshopGraderInfos()) {
            if (OfferedWorkshop.doTwoOfferedWorkshopTimeIntervalsOverlap(workshopGraderInfo.getOfferedWorkshop(), offeredWorkshop)) {
                return new ResponseEntity<>("This overlaps one of the workshops of the grader", HttpStatus.BAD_REQUEST);
            }
        }
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo != null) {
            return new ResponseEntity<>("You are the workshop manager", HttpStatus.FORBIDDEN);
        }


        AttenderWorkshopConnection attenderWorkshopConnection = ((Attender) user.getRole("Attender")).getAttenderWorkshopConnection();

        if (!offeredWorkshop.hasAtendeeRequested(attenderWorkshopConnection)) {
            return new ResponseEntity<>("The grader is already the attendee of this workshop", HttpStatus.FORBIDDEN);
        }

        GraderRequestForm graderRequestForm = offeredWorkshop.getGraderRequestForm();

        GraderFormApplicant graderFormApplicant = new GraderFormApplicant();
        graderFormApplicant.setWorkshopGrader(graderWorkshopConnection);
        graderWorkshopConnection.addGraderFormApplicant(graderFormApplicant);


        List<Answer> answers = new ArrayList<>();

        for (AnswerQuestionContext answerQuestionContext : registerRequestContext.getAnswerQuestionContexts()) {
            Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());

            if (!optionalQuestion.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Question question = optionalQuestion.get();

            if (question.getForm().getId() != graderRequestForm.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Answer answer = new Answer();

            String type = answerQuestionContext.getType();
            LinkedHashMap<String, Object> answerDataObject = answerQuestionContext.getAnswerData();
            AnswerData answerData = null;

            if (type.equalsIgnoreCase("TextAnswer")) {
                TextAnswer textAnswer = new TextAnswer();
                textAnswer.setText((String) answerDataObject.get("text"));
                answerData = textAnswer;
            } else if (type.equalsIgnoreCase("ChoiceAnswer")) {
                ChoiceAnswer choiceAnswer = new ChoiceAnswer();
                choiceAnswer.setChoice((Integer) answerDataObject.get("choice"));
                answerData = choiceAnswer;
            } else if (type.equalsIgnoreCase("FileAnswer")) {
                FileAnswer fileAnswer = new FileAnswer();

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                fileAnswer.setFileName(fileName);
                fileAnswer.setFileType(multipartFile.getContentType());
                fileAnswer.setData(multipartFile.getBytes());

                answerData = fileAnswer;


            } else {
                return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
            }


            answer.addAnswerData(answerData);

            answer.setFormApplicant(graderFormApplicant);
            graderFormApplicant.addAnswers(answer);

            question.addAnswer(answer);
            answer.setQuestion(question);

            answers.add(answer);

        }
        for (Answer answer : answers) {
            answerRepository.save(answer);
        }

        Request request = new Request();
        request.setRequestable(offeredWorkshop);
        offeredWorkshop.addRequest(request);
        Grader grader = graderWorkshopConnection.getGrader();
        request.setRequester(grader);
        grader.addRequest(request);
        request.addRequestData(graderRequestForm);
        request.setState(RequestState.Pending);
        requestRepository.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/grader/request/offeringWorkshop/{offeringWorkshopId}/request")
    public ResponseEntity<Object> revertAGraderRequestForAnOfferedWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication) {

        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Grader grader = graderWorkshopConnection.getGrader();
        Request request = null;

        for (Request request1 : offeredWorkshop.getRequests()) {

            if (request1.getRequester().equals(grader)) {
                request = request1;
                break;
            }

        }

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        GraderRequestForm graderRequestForm = (GraderRequestForm) request.getRequestData().get(0);

        List<Question> questions = graderRequestForm.getQuestions();

        GraderFormApplicant graderFormApplicant = null;

        List<Answer> answers = questions.get(0).getAnswers();
        for (Answer answer : answers) {

            if (graderWorkshopConnection.getGraderFormApplicants().contains(answer.getFormApplicant())) {
                graderFormApplicant = (GraderFormApplicant) answer.getFormApplicant();
                break;
            }

        }

        if (graderFormApplicant == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        for (Answer answer : graderFormApplicant.getAnswers()){

            Question answeredQuestion = answer.getQuestion();

            answeredQuestion.getAnswers().remove(answer);

            questionRepsoitory.save(answeredQuestion);

            answer.setQuestion(null);
            answerRepository.save(answer);
        }

        graderWorkshopConnection.getGraderFormApplicants().remove(graderFormApplicant);

        workshopGraderRepository.save(graderWorkshopConnection);

        graderFormApplicant.setWorkshopGrader(null);

        formApplicantRepository.save(graderFormApplicant);

        formApplicantRepository.delete(graderFormApplicant);

        offeredWorkshop.getRequests().remove(request);

        offeringWorkshopRepository.save(offeredWorkshop);

        request.setRequestable(null);

        grader.getRequests().remove(request);

        graderRepository.save(grader);

        request.setRequester(null);

        request.setRequestData(null);

        requestRepository.delete(request);

        return new ResponseEntity<>(HttpStatus.OK);

    }


    public WorkshopManagerInfo findWorkshopManagerInfoOfWorkshop(OfferedWorkshop offeredWorkshop, WorkshopManager workshopManager) {

        for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {

            if (workshopManagerInfo.getWorkshopManager().getId() == workshopManager.getId()) {
                return workshopManagerInfo;
            }
        }

        return null;
    }

}
