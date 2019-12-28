package com.atelier.atelier.entity.FormService;

import javax.persistence.*;

@Entity
@Table
public class ChoiceAnswer implements AnswerData{

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "choice", nullable = false)
    private int choice;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
