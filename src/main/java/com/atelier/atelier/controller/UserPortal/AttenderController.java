package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.OfferingWorkshopContext;
import com.atelier.atelier.context.RegisterRequestContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.hibernate.jdbc.Work;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

// TODO SYS ADMIN HAS TO HAVE PATHS FOR CREATING ROLES FOR USERS
// TODO FOR REGISTRATION RETRIEVE GRADERS INFO LIST AND ATTENDER'S INFO LIST FOR A OFFERING WORKSHOP AND
// TODO HAVE THE WORKSHOP MANGER FORM GROUPS BASED ON THESE LISTS
// TODO CHECK ATTENDER'S PERMISSION'S FOR ENROLLMENT (SEE IF HE HAS PASSED THE PRE REQUISTIES, IF HE HAS ENROLLED IN THE SAME WORKSHOP)

@RestController
@RequestMapping("/attendees")
public class AttenderController {

    private AttenderRepository attenderRepository;
    private UserRepository userRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;
    private QuestionRepsoitory questionRepsoitory;
    private AnswerRepository answerRepository;
    private RequestRepository requestRepository;

    public AttenderController(AttenderRepository attenderRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository, RequestRepository requestRepository) {
        this.attenderRepository = attenderRepository;
        this.userRepository = userRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
        this.requestRepository = requestRepository;
    }

    @GetMapping("/attendee")
    public ResponseEntity<Object> getAttendeeRole(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The attendee role was not found for the user", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        return new ResponseEntity<>(attenderRole, HttpStatus.OK);
    }

    @GetMapping("/attendee/workshops")
    public ResponseEntity<Object> getAttenderWorkshops(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The user has no attendee role.", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        AttenderWorkshopConnection attenderWorkshopConnection = attenderRole.getAttenderWorkshopConnection();
        if ( attenderWorkshopConnection == null) {
            return new ResponseEntity<>("The user has no attendee workshop connection.", HttpStatus.NO_CONTENT);
        }
        List<WorkshopAttenderInfo> workshopAttenderInfos = attenderWorkshopConnection.getWorkshopAttenderInfos();
        if (workshopAttenderInfos.isEmpty()) {
            return new ResponseEntity<>("The user has no attendee workshop attendee info", HttpStatus.NO_CONTENT);
        }
        List<OfferedWorkshop> workshops = new ArrayList<>();
        for (WorkshopAttenderInfo workshopAttenderInfo : workshopAttenderInfos) {
            workshops.add(workshopAttenderInfo.getOfferedWorkshop());
        }
        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }


//
//    @PostMapping("/attendee/{offeringWorkshopId}")
//    public ResponseEntity<Object> enrollAttendeeAtOfferingWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication) {
//        User user = User.getUser(authentication, userRepository);
//        Role role = user.getRole("Attender");
//        if (role == null) {
//            return new ResponseEntity<>("The user does not have an attendee role.", HttpStatus.NO_CONTENT);
//        }
//        Attender attenderRole = (Attender) role;
//        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
//        if (!optionalOfferedWorkshop.isPresent()) {
//            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
//        }
//        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
//        AttenderWorkshopConnection attenderWorkshopConnection = attenderRole.getAttenderWorkshopConnection();
//
//        WorkshopAttenderInfo workshopAttenderInfo = new WorkshopAttenderInfo();
//        workshopAttenderInfo.setOfferedWorkshop(offeredWorkshop);
//        workshopAttenderInfo.setWorkshopAttender(attenderWorkshopConnection);
//        attenderWorkshopConnection.addWorkshopAttenderInfo(workshopAttenderInfo);
//        offeredWorkshop.addWorkshopAttenderInfo(workshopAttenderInfo);
//        workshopAttenderInfoRepository.save(workshopAttenderInfo);
//        return new ResponseEntity<>(workshopAttenderInfo, HttpStatus.OK);
//
//
//    }


    @GetMapping("/attendee/request/offeringWorkshop/{id}")
    public ResponseEntity<Object> getGraderRequestForm(@PathVariable long id){
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop, HttpStatus.OK);

    }


    @PostMapping("/attendee/request/offeringWorkshop/{id}/answer")
    public ResponseEntity<Object> answerAttenderRegisterForm(@PathVariable long id, Authentication authentication, @RequestBody RegisterRequestContext registerRequestContext){
        AttenderWorkshopConnection attenderWorkshopConnection = getAttendeeWorkshopConnectionFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (!offeredWorkshop.hasAtendeeRequested(attenderWorkshopConnection)){
            return new ResponseEntity<>("The attendee is already in this workshop", HttpStatus.BAD_REQUEST);
        }
        AttenderRegisterForm attenderRegisterForm = offeredWorkshop.getAttenderRegisterForm();

        AttenderFormApplicant attenderFormApplicant = new AttenderFormApplicant();
        attenderFormApplicant.setWorkshopAttender(attenderWorkshopConnection);
        attenderWorkshopConnection.addAttenderFormApplicants(attenderFormApplicant);

        List<Answer> answers = new ArrayList<>();

        for ( AnswerQuestionContext answerQuestionContext : registerRequestContext.getAnswerQuestionContexts()){
            Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());

            if (!optionalQuestion.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Question question = optionalQuestion.get();

            if ( question.getForm().getId() != attenderRegisterForm.getId()){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Answer answer = new Answer();

            String type = answerQuestionContext.getType();
            LinkedHashMap<String, Object> answerDataObject = answerQuestionContext.getAnswerData();
            AnswerData answerData = null;

            if (type.equalsIgnoreCase("TextAnswer")){
                TextAnswer textAnswer = new TextAnswer();
                textAnswer.setText((String) answerDataObject.get("text"));
                answerData = textAnswer;
            }

            else if ( type.equalsIgnoreCase("ChoiceAnswer")){
                ChoiceAnswer choiceAnswer = new ChoiceAnswer();
                choiceAnswer.setChoice((Integer) answerDataObject.get("choice"));
                answerData = choiceAnswer;
            }
            else {
                return new ResponseEntity<>("Type not supported",HttpStatus.BAD_REQUEST);
            }
            //TODO fix file answer

//                    else if (type.equalsIgnoreCase("FileAnswer")){
//                        FileAnswer fileAnswer = new FileAnswer();
//                        fileAnswer.
//                    }


            answer.addAnswerData(answerData);

            answer.setFormApplicant(attenderFormApplicant);
            attenderFormApplicant.addAnswers(answer);

            question.addAnswer(answer);
            answer.setQuestion(question);

            answers.add(answer);

        }
        for(Answer answer: answers){
            answerRepository.save(answer);
        }
        Request request = new Request();
        request.setRequestable(offeredWorkshop);
        offeredWorkshop.addRequest(request);
        Attender attender = attenderWorkshopConnection.getAttender();
        request.setRequester(attender);
        attender.addRequest(request);
        request.addRequestData(attenderRegisterForm);
        request.setState(RequestState.Pending);
        requestRepository.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private AttenderWorkshopConnection getAttendeeWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Attender attender = (Attender) user.getRole("Attender");
        return attender.getAttenderWorkshopConnection();
    }

}
