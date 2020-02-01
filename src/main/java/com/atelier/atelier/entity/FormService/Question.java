package com.atelier.atelier.entity.FormService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answerable> answerables;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "form_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Form form;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answerable> getAnswerables() {
        return answerables;
    }

    public void setAnswerables(List<Answerable> answerables) {
        this.answerables = answerables;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void addAnswer(Answer answer){
        if(answers == null){
            answers = new ArrayList<>();
        }
        answers.add(answer);
    }


    public void addAnswerable(Answerable answerable){
        if(answerables == null){
            answerables = new ArrayList<>();
        }

        answerables.add(answerable);
    }

    public boolean isChoicable(){

        return !answerables.isEmpty();
    }

}
