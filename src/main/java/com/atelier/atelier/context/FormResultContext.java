package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Answer;
import com.atelier.atelier.entity.FormService.Question;

public class FormResultContext {
    private Answer answer;
    private Question question;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
