package com.atelier.atelier.entity.FormService;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "FilledAnswer")
public class FilledAnswer extends Answer {

    @ManyToOne
    @JoinColumn(name = "form_filler_id")
    private FormFiller formFiller;

}
