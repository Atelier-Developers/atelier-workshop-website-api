package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.FormAnswerContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.GraderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
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

    public WorkshopGraderController(OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository) {
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
    }

    @PostMapping("/offeringWorkshop/{id}/workshopForm/answer")
    public ResponseEntity<Object> answerToWorkshopForm(@PathVariable long id, Authentication authentication, @RequestBody FormAnswerContext formAnswerContext) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        GraderWorkshopConnection graderWorkshopConnection = getGraderWorkshopConnectionFromAuthentication(authentication);
        WorkshopGraderInfo workshopGraderInfo = graderWorkshopConnection.getWorkshopGraderInfoOfferedWorkshop(offeredWorkshop);
        if (workshopGraderInfo == null) {
            return new ResponseEntity<>("You aren't grader of this workshop", HttpStatus.FORBIDDEN);
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
            WorkshopAttenderFormApplicant workshopAttenderFormApplicant = new WorkshopAttenderFormApplicant();
            WorkshopGraderFormFiller workshopGraderFormFiller = new WorkshopGraderFormFiller();
            workshopGraderFormFiller.setWorkshopGraderInfo(workshopGraderInfo);
            workshopAttenderFormApplicant.setWorkshopAttenderInfo(workshopAttenderInfo);

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
                } else {
                    return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
                }
                //TODO fix file answer
                filledAnswer.addAnswerData(answerData);

                filledAnswer.setFormFiller(workshopGraderFormFiller);
                workshopGraderFormFiller.addAnswer(filledAnswer);

                filledAnswer.setFormApplicant(workshopAttenderFormApplicant);
                workshopAttenderFormApplicant.addAnswers(filledAnswer);

                question.addAnswer(filledAnswer);
                filledAnswer.setQuestion(question);
                answerRepository.save(filledAnswer);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

    }

    private GraderWorkshopConnection getGraderWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Grader grader = (Grader) user.getRole("Grader");
        return grader.getGraderWorkshopConnection();
    }

}
