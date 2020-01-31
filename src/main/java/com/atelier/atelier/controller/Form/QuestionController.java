package com.atelier.atelier.controller.Form;


import com.atelier.atelier.entity.FormService.Question;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private QuestionRepsoitory questionRepsoitory;

    public QuestionController(QuestionRepsoitory questionRepsoitory) {
        this.questionRepsoitory = questionRepsoitory;
    }

    @GetMapping("/question")
    public ResponseEntity<Object> showAllQuestions() {
        return new ResponseEntity<>(questionRepsoitory.findAll(), HttpStatus.OK);
    }


//    @PostMapping("/question/answer")
//    public ResponseEntity<Object> answersToQuestions(@RequestBody List<AnswerQuestionContext> answerQuestionContexts, @PathVariable long id) {
//        for ( AnswerQuestionContext answerQuestionContext : answerQuestionContexts){
//            Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());
//            if ( !optionalQuestion.isPresent() ){
//                return new ResponseEntity<>("Question not found!", HttpStatus.NO_CONTENT);
//            }
//            Question question = optionalQuestion.get();
//            Answer answer = answerQuestionContext.getAnswer();
//            question.addAnswer(answer);
//            answer.setQuestion(question);
//            questionRepsoitory.save(question);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @GetMapping("/question/{id}/answer")
    public ResponseEntity<Object> getAllAnswersOfQuestion(@PathVariable long id) {
        Optional<Question> optionalQuestion = questionRepsoitory.findById(id);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>("Question not found!", HttpStatus.NO_CONTENT);
        }
        Question question = optionalQuestion.get();
        return new ResponseEntity<>(question.getAnswers(), HttpStatus.OK);
    }




}
