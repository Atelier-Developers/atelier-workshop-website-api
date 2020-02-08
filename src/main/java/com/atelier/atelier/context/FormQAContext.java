package com.atelier.atelier.context;

import java.util.List;

public class FormQAContext {

    private String name;
    private List<QAContext> questionsAndAnswers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QAContext> getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }

    public void setQuestionsAndAnswers(List<QAContext> questionsAndAnswers) {
        this.questionsAndAnswers = questionsAndAnswers;
    }
}
