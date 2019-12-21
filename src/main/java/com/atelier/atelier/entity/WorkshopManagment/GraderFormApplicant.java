package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderFormApplicant")
public class GraderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_grader_id")
    private WorkshopGrader workshopGrader;

}
