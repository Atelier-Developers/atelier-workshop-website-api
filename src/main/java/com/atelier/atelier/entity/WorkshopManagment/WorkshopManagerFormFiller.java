package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormFiller;

import javax.persistence.*;


@Entity
@Table
@DiscriminatorValue(value = "WorkshopManagerFormFiller")
public class WorkshopManagerFormFiller extends FormFiller {

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id")
    private WorkshopManager workshopManager;

}
