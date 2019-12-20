package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "WorkshopGraderFormApplicant")

public class WorkshopGraderFormApplicant extends FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_grader_info_id")
    private WorkshopGraderInfo workshopGraderInfo;

}
