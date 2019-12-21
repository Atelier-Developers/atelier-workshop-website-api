package com.atelier.atelier.entity.FormService;

import javax.persistence.*;
import javax.persistence.criteria.From;
import java.util.List;

@Entity
@Table
public class Question {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @OneToMany(mappedBy = "question")
    private List<Answerable> answerables;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
}
