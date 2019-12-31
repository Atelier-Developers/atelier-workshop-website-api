package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderFormApplicant")
public class GraderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_grader_id", unique = true)
    private WorkshopGrader workshopGrader;

    public WorkshopGrader getWorkshopGrader() {
        return workshopGrader;
    }

    public void setWorkshopGrader(WorkshopGrader workshopGrader) {
        this.workshopGrader = workshopGrader;
    }
}
