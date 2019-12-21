package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "WorkshopAttenderFormApplicant")
public class WorkshopAttenderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_attender_info_id")
    private WorkshopAttenderInfo workshopAttenderInfo;
}
