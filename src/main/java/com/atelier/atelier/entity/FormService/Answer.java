package com.atelier.atelier.entity.FormService;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "answer_type")

public class Answer {

    @Id
    @GeneratedValue
    private long id;

    @ManyToAny(metaColumn = @Column(name = "answer_data_type"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
            @MetaValue(targetEntity = FileAnswer.class, value = "FileAnswer"),
            @MetaValue(targetEntity = ChoiceAnswer.class, value = "ChoiceAnswer"),
            @MetaValue(targetEntity = TextAnswer.class, value = "TextAnswer")
    })
    @Cascade( { org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(name = "answer_answer_data",
            joinColumns = @JoinColumn(name = "answer_id" ),
            inverseJoinColumns = @JoinColumn(name = "answer_data_id")
    )
    private List<AnswerData> answerData;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_applicant_id")
    private FormApplicant formApplicant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AnswerData> getAnswerData() {
        return answerData;
    }

    public void setAnswerData(List<AnswerData> answerData) {
        this.answerData = answerData;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public FormApplicant getFormApplicant() {
        return formApplicant;
    }

    public void setFormApplicant(FormApplicant formApplicant) {
        this.formApplicant = formApplicant;
    }
}
