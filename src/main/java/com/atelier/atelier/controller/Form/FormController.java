package com.atelier.atelier.controller.Form;


import com.atelier.atelier.context.FormAnswerContext;
import com.atelier.atelier.entity.FormService.Answer;
import com.atelier.atelier.entity.FormService.Answerable;
import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.FormService.Question;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forms")
public class FormController {

    private FormRepository formRepository;
    private QuestionRepsoitory questionRepsoitory;

    public FormController(FormRepository formRepository, QuestionRepsoitory questionRepsoitory) {
        this.formRepository = formRepository;
        this.questionRepsoitory = questionRepsoitory;
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



}

