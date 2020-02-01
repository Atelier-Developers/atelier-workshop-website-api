package com.atelier.atelier.controller.Form;


import com.atelier.atelier.context.FormResultContext;
import com.atelier.atelier.entity.FormService.Answer;
import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.FormService.FormApplicant;
import com.atelier.atelier.entity.FormService.Question;
import com.atelier.atelier.entity.UserPortalManagment.Attender;
import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.FormApplicantRepository;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.GraderRequestFormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forms")
public class FormController {

    private FormRepository formRepository;
    private QuestionRepsoitory questionRepsoitory;
    private GraderRequestFormRepository graderRequestFormRepository;
    private FormApplicantRepository formApplicantRepository;

    public FormController(FormApplicantRepository formApplicantRepository, GraderRequestFormRepository graderRequestFormRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory) {
        this.formRepository = formRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.graderRequestFormRepository = graderRequestFormRepository;
        this.formApplicantRepository = formApplicantRepository;
    }

    @GetMapping("/form")
    public ResponseEntity<Object> showAllForm() {
        return new ResponseEntity<>(formRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/form/{id}")
    public ResponseEntity<Object> getSingleForm(@PathVariable long id) {
        Optional<Form> optionalForm = formRepository.findById(id);
        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Form form = optionalForm.get();
        return new ResponseEntity<>(form, HttpStatus.OK);
    }


    @GetMapping("/form/{formId}/questions")
    public ResponseEntity<Object> showAllQuestions(@PathVariable long formId) {
        Optional<Form> optionalForm = formRepository.findById(formId);
        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Form form = optionalForm.get();
        return new ResponseEntity<>(form.getQuestions(), HttpStatus.OK);
    }


    //TODO fix delete path for form
    @DeleteMapping("/form/{formId}")
    public ResponseEntity<Object> deleteForm(@PathVariable long formId){
        Optional<Form> optionalForm = formRepository.findById(formId);
        if (!optionalForm.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        GraderRequestForm form = (GraderRequestForm) optionalForm.get();

        graderRequestFormRepository.delete(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/form/{formId}/result/{appId}")
    public ResponseEntity<Object> showApplicantResultForAForm(@PathVariable long formId, @PathVariable long appId){

        Optional<FormApplicant> optionalFormApplicant = formApplicantRepository.findById(appId);

        if (!optionalFormApplicant.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FormApplicant formApplicant = optionalFormApplicant.get();

        Optional<Form> optionalForm = formRepository.findById(formId);

        if (!optionalForm.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Form form = optionalForm.get();

        if (((form instanceof GraderRequestForm) || (form instanceof GraderEvaluationForm) ) && (formApplicant instanceof GraderFormApplicant)){
            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for (Question question : questions){
                for (Answer answer : question.getAnswers()){
                    GraderFormApplicant graderFormApplicant = (GraderFormApplicant) answer.getFormApplicant();
                    if (graderFormApplicant.getId() == formApplicant.getId()){
                        FormResultContext formResultContext = new FormResultContext();
                        formResultContext.setQuestion(question);
                        formResultContext.setAnswer(answer);
                        formResultContexts.add(formResultContext);
                    }
                }
            }

            return new ResponseEntity<>(formResultContexts, HttpStatus.OK);

        }

        else if (((form instanceof AttenderRegisterForm) || (form instanceof WorkshopForm)) && (formApplicant instanceof AttenderFormApplicant)){

            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for (Question question : questions){
                for (Answer answer : question.getAnswers()){
                    AttenderFormApplicant attenderFormApplicant = (AttenderFormApplicant) answer.getFormApplicant();
                    if (attenderFormApplicant.getId() == formApplicant.getId()){
                        FormResultContext formResultContext = new FormResultContext();
                        formResultContext.setQuestion(question);
                        formResultContext.setAnswer(answer);
                        formResultContexts.add(formResultContext);
                    }
                }
            }

            return new ResponseEntity<>(formResultContexts, HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

