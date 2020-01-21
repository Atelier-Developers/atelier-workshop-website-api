package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "AttenderFormApplicant")
public class AttenderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id")
    private WorkshopAttender workshopAttender;

    public WorkshopAttender getWorkshopAttender() {
        return workshopAttender;
    }

    public void setWorkshopAttender(WorkshopAttender workshopAttender) {
        this.workshopAttender = workshopAttender;
    }
}
