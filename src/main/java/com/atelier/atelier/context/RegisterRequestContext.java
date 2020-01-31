package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Answer;

import java.util.List;

public class RegisterRequestContext {

    private List<AnswerQuestionContext> answerQuestionContexts;


    public List<AnswerQuestionContext> getAnswerQuestionContexts() {
        return answerQuestionContexts;
    }

    public void setAnswerQuestionContexts(List<AnswerQuestionContext> answerQuestionContexts) {
        this.answerQuestionContexts = answerQuestionContexts;
    }
}
