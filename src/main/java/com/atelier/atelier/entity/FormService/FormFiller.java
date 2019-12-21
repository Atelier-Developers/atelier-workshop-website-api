package com.atelier.atelier.entity.FormService;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "form_filler_type")
public abstract class FormFiller {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "formFiller")
    private List<FilledAnswer> answers;
}
