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
}
