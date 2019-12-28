package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.RegisterRequestContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.GraderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.role.GraderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopGraderInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


// TODO ADD A GRADER WOKRSHOP CONNECTION WHEN THE ROLE IS BEING CREATED IN THE USER CONTROLLER (FOR TESTING)
// TODO WHEN ADDING AN OFFERING WORKSHOP TO THE GRADER, CHECK IF THE GRADER IS NOT AN ATTENDER IN THE SAME OFFERING WORKSHOP,
// TODO ALSO THEY AREN'T A GRADER IN THE SAME OFFERING WORKSHOP

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

    public GraderController(UserRepository userRepository, GraderRepository graderRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopGraderInfoRepository workshopGraderInfoRepository, FormRepository formRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.graderRepository = graderRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
        this.formRepository = formRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
        this.requestRepository =  requestRepository;
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
        List<OfferedWorkshop> workshops = new ArrayList<>();
        for (WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos) {
            workshops.add(workshopGraderInfo.getOfferedWorkshop());
        }

        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

//    @PostMapping("/grader/{offeringWorkshopId}")
//    public ResponseEntity<Object> enrollGraderAtOfferingWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication) {
//        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
//        if (!optionalOfferedWorkshop.isPresent()) {
//            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
//        }
//        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
//        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);
//
//        return new ResponseEntity<>(workshopGraderInfo, HttpStatus.OK);
//    }

    private GraderWorkshopConnection getGraderWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Grader grader = (Grader) user.getRole("Grader");
        return grader.getGraderWorkshopConnection();
    }


    @GetMapping("/grader/request/offeringWorkshop/{id}")
    public ResponseEntity<Object> showGraderRequestForm(@PathVariable long id ){
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getGraderRequestForm(), HttpStatus.OK);
    }


    @PostMapping("/grader/request/offeringWorkshop/{id}/answer")
    public ResponseEntity<Object> answerGraderRequestForm(@PathVariable long id, Authentication authentication, @RequestBody RegisterRequestContext registerRequestContext){


        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (!offeredWorkshop.hasGraderRequested(graderWorkshopConnection)){
            return new ResponseEntity<>("The grader is already in this workshop", HttpStatus.BAD_REQUEST);
        }

        GraderRequestForm graderRequestForm = offeredWorkshop.getGraderRequestForm();

        GraderFormApplicant graderFormApplicant = new GraderFormApplicant();
        graderFormApplicant.setWorkshopGrader(graderWorkshopConnection);
        graderWorkshopConnection.addGraderFormApplicant(graderFormApplicant);


        List<Answer> answers = new ArrayList<>();

        for ( AnswerQuestionContext answerQuestionContext : registerRequestContext.getAnswerQuestionContexts()){
            Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());

            if (!optionalQuestion.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Question question = optionalQuestion.get();

            if ( question.getForm().getId() != graderRequestForm.getId()){
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

            answer.setFormApplicant(graderFormApplicant);
            graderFormApplicant.addAnswers(answer);

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
        Grader grader = graderWorkshopConnection.getGrader();
        request.setRequester(grader);
        grader.addRequest(request);
        request.addRequestData(graderRequestForm);
        request.setState(RequestState.Pending);
        requestRepository.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
