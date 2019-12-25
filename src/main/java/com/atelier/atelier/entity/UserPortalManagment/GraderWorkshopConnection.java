package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGrader;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderWorkshopConnection")
public class GraderWorkshopConnection extends WorkshopGrader{


    @OneToOne
    @JoinColumn(name = "grader_id", unique = true)
    private Grader grader;

    @JsonIgnore
    public Grader getGrader() {
        return grader;
    }

    public void setGrader(Grader grader) {
        this.grader = grader;
    }
}
