package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGrader;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderWorkshopConnection")
public class GraderWorkshopConnection extends WorkshopGrader{


    @OneToOne
    @JoinColumn(name = "grader_id")
    private Grader grader;
}
