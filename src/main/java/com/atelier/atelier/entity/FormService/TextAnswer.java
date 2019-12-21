package com.atelier.atelier.entity.FormService;


import javax.persistence.*;

@Entity
@Table
public class TextAnswer implements AnswerData{

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "text", nullable = false)
    private String text;
}
