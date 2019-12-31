package com.atelier.atelier.entity.FormService;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "form_applicant_type")
@DiscriminatorOptions(force = true)

public abstract class FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "formApplicant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Answer> answers;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswers(Answer answer) {
        if (answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answer);
    }
}
