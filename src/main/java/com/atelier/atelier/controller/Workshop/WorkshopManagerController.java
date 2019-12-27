package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.AnswerQuestionContext;
import com.atelier.atelier.context.FormAnswerContext;
import com.atelier.atelier.context.OfferingWorkshopContext;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.UserPortalManagment.ManagerWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopGraderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshopManagers")
public class WorkshopManagerController {

    private WorkshopRepository workshopRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private UserRepository userRepository;
    private FormRepository formRepository;
    private QuestionRepsoitory questionRepsoitory;
    private WorkshopGraderInfoRepository workshopGraderInfoRepository;
    private AnswerRepository answerRepository;

    public WorkshopManagerController(WorkshopRepository workshopRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory, WorkshopGraderInfoRepository workshopGraderInfoRepository, AnswerRepository answerRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.formRepository = formRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
        this.answerRepository = answerRepository;
    }

    @GetMapping("/offeringWorkshop")
    public ResponseEntity<Object> showAllOfferedWorkshop(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("ManagerWorkshopConnection");
        if (role == null) {
            return new ResponseEntity<>("User has no workshop manager role", HttpStatus.NO_CONTENT);
        }
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) role;
        return new ResponseEntity<>(managerWorkshopConnection.getOfferedWorkshops(), HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop")
    public ResponseEntity<Object> addOfferingWorkshop(@RequestBody OfferingWorkshopContext offeringWorkshopContext, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        long wid = offeringWorkshopContext.getWorkshopId();
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(wid);
        if (!optionalWorkshop.isPresent()) {
            return new ResponseEntity<>("The workshop with the id provided does not exist. ", HttpStatus.NO_CONTENT);
        }
        Workshop workshop = optionalWorkshop.get();
        OfferedWorkshop offeredWorkshop = offeringWorkshopContext.getOfferedWorkshop();
        offeredWorkshop.setWorkshop(workshop);
        workshop.addOfferingWorkshop(offeredWorkshop); // BI DIRECTIONAL MAPPING
        offeredWorkshop.setWorkshopManager(managerWorkshopConnection);
        offeringWorkshopRepository.save(offeredWorkshop);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/offeringWorkshop/{id}")
    public ResponseEntity<Object> showAllOfferedWorkshopForms(Authentication authentication, @PathVariable Long id) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                return new ResponseEntity<>(offeredWorkshop, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/offeringWorkshop/{id}/graderEvaluationForm")
    public ResponseEntity<Object> makeGraderEvalForm(@PathVariable long id, Authentication authentication, @RequestBody GraderEvaluationForm graderEvaluationForm) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                graderEvaluationForm.setOfferedWorkshop(offeredWorkshop);
                System.out.println(graderEvaluationForm.getOfferedWorkshop().getId());
                offeredWorkshop.setGraderEvaluationForm(graderEvaluationForm);
                formRepository.save(graderEvaluationForm);
                // TODO integrity
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    @PostMapping("/offeringWorkshop/{id}/graderEvaluationForm/answer")
    public ResponseEntity<Object> answerToGraderEvalForm(@PathVariable long id, @RequestBody FormAnswerContext formAnswerContext, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            Optional<Form> optionalForm = formRepository.findById(formAnswerContext.getFormId());
            if (!optionalForm.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Form form = optionalForm.get();
            Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(formAnswerContext.getApplicantId());

            if (!optionalWorkshopGraderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();

            if (!(offeredWorkshop.getWorkshopGraderInfos().contains(workshopGraderInfo))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                for (AnswerQuestionContext answerQuestionContext : formAnswerContext.getAnswerQuestionContexts()) {
                    Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());
                    if (!optionalQuestion.isPresent()) {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    }
                    Question question = optionalQuestion.get();
                    Answer answer = answerQuestionContext.getAnswer();
                    if (question.getForm().getId() != form.getId() && !(answer instanceof FilledAnswer)) {
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                    FilledAnswer filledAnswer = (FilledAnswer) answer;
                    WorkshopManagerFormFiller workshopManagerFormFiller = new WorkshopManagerFormFiller();

                    workshopManagerFormFiller.setWorkshopManager(managerWorkshopConnection);
                    managerWorkshopConnection.addFormFiller(workshopManagerFormFiller);

                    filledAnswer.setFormFiller(workshopManagerFormFiller);
                    workshopManagerFormFiller.addAnswer(filledAnswer);

                    WorkshopGraderFormApplicant workshopGraderFormApplicant = new WorkshopGraderFormApplicant();
                    workshopGraderFormApplicant.setWorkshopGraderInfo(workshopGraderInfo);
                    workshopGraderInfo.addWorkshopGraderFormApplicants(workshopGraderFormApplicant);

                    filledAnswer.setFormApplicant(workshopGraderFormApplicant);
                    workshopGraderFormApplicant.addAnswers(filledAnswer);

                    question.addAnswer(filledAnswer);
                    filledAnswer.setQuestion(question);


                    answerRepository.save(filledAnswer);
                }
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    private ManagerWorkshopConnection getMangerFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        return managerWorkshopConnection;
    }
}