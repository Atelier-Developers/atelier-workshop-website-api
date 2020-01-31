package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Answer;

import java.util.LinkedHashMap;

public class AnswerQuestionContext {
    private long questionId;
    private String type;
    private LinkedHashMap<String, Object> answerData;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public LinkedHashMap<String, Object> getAnswerData() {
        return answerData;
    }

    public void setAnswerData(LinkedHashMap<String, Object> answerData) {
        this.answerData = answerData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
