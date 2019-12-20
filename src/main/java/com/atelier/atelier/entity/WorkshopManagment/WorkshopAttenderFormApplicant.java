package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "WorkshopAttenderFormApplicant")
public class WorkshopAttenderFormApplicant extends FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_info_id")
    private WorkshopAttenderInfo workshopAttenderInfo;
}
