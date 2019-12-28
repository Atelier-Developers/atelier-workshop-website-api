package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Question;

import java.util.List;

public class FormQuestionContext {

    private List<Question> question;
    private long formId;

    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }
}
