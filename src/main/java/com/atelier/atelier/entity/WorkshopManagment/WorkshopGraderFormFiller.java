package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormFiller;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "WorkshopGraderFormFiller")
public class WorkshopGraderFormFiller extends FormFiller {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_grader_info_id")
    private WorkshopGraderInfo workshopGraderInfo;
}
