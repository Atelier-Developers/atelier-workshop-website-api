package com.atelier.atelier.controller.Form;


import com.atelier.atelier.context.FormResultContext;
import com.atelier.atelier.entity.FormService.Answer;
import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.FormService.FormApplicant;
import com.atelier.atelier.entity.FormService.Question;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forms")
public class FormController {

    private FormRepository formRepository;
    private QuestionRepsoitory questionRepsoitory;
    private GraderRequestFormRepository graderRequestFormRepository;
    private GraderEvaluationFormRepository graderEvaluationFormRepository;
    private FormApplicantRepository formApplicantRepository;

    public FormController(GraderEvaluationFormRepository graderEvaluationFormRepository, FormApplicantRepository formApplicantRepository, GraderRequestFormRepository graderRequestFormRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory) {
        this.formRepository = formRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.graderRequestFormRepository = graderRequestFormRepository;
        this.formApplicantRepository = formApplicantRepository;
        this.graderEvaluationFormRepository = graderEvaluationFormRepository;
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


    //TODO fix delete path for form (IS OKAY FOR WORKSHOP AND EVALUATION, BUT NOT OKAY FOR REGISTER AND REQUEST CUZ THEY HAVE NULL AS OFFERING WORKSHOP)
    @DeleteMapping("/form/{formId}")
    public ResponseEntity<Object> deleteForm(@PathVariable long formId){
        Optional<Form> optionalForm = formRepository.findById(formId);
        if (!optionalForm.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Form form = optionalForm.get();

        formRepository.delete(form);
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

