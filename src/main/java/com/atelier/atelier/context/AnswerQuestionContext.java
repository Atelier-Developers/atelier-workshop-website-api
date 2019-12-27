package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Answer;

public class AnswerQuestionContext {
    private long questionId;
    private Answer answer;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
