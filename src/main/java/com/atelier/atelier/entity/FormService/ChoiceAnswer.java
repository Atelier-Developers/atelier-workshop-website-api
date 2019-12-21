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

}
