package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGrader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderWorkshopConnection")
public class GraderWorkshopConnection extends WorkshopGrader{


    @OneToOne
    @JoinColumn(name = "grader_id", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Grader grader;


    public Grader getGrader() {
        return grader;
    }

    public void setGrader(Grader grader) {
        this.grader = grader;
    }
}
