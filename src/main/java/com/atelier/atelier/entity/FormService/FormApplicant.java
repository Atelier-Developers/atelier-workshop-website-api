package com.atelier.atelier.entity.FormService;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "form_applicant_type")
public abstract class FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "formApplicant")
    private List<Answer> answers;
}
