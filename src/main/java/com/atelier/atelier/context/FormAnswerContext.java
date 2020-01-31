package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Answer;

import java.util.List;

public class FormAnswerContext {
    private long formId;
    private long applicantId;
    private List<AnswerQuestionContext> answerQuestion;


    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(long applicantId) {
        this.applicantId = applicantId;
    }

    public List<AnswerQuestionContext> getAnswerQuestion() {
        return answerQuestion;
    }

    public void setAnswerQuestion(List<AnswerQuestionContext> answerQuestion) {
        this.answerQuestion = answerQuestion;
    }
}

