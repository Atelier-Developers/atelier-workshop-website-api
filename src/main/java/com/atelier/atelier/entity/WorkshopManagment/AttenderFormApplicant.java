package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "AttenderFormApplicant")
public class AttenderFormApplicant extends FormApplicant {

    //todo nullable was picked off
    @ManyToOne
    @JoinColumn(name = "workshop_attender_id", unique = true)
    private WorkshopAttender workshopAttender;

    public WorkshopAttender getWorkshopAttender() {
        return workshopAttender;
    }

    public void setWorkshopAttender(WorkshopAttender workshopAttender) {
        this.workshopAttender = workshopAttender;
    }
}
