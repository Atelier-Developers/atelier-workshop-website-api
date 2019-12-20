package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "AttenderFormApplicant")
public class AttenderFormApplicant extends FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id")
    private WorkshopAttender workshopAttender;
}
