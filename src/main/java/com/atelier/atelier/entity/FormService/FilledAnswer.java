package com.atelier.atelier.entity.FormService;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "FilledAnswer")
public class FilledAnswer extends Answer {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_filler_id", nullable = false)
    private FormFiller formFiller;


    public FormFiller getFormFiller() {
        return formFiller;
    }

    public void setFormFiller(FormFiller formFiller) {
        this.formFiller = formFiller;
    }
}
