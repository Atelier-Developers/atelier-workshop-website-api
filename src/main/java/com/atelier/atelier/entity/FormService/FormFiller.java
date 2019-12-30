package com.atelier.atelier.entity.FormService;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "form_filler_type")
@DiscriminatorOptions(force = true)

public abstract class FormFiller {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "formFiller")
    private List<FilledAnswer> answers;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FilledAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FilledAnswer> answers) {
        this.answers = answers;
    }


    public void addAnswer(FilledAnswer filledAnswer){
        if ( answers == null ){
            answers = new ArrayList<>();
        }
        answers.add(filledAnswer);
    }
}
