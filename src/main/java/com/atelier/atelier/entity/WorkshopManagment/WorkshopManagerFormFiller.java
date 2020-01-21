package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormFiller;

import javax.persistence.*;


@Entity
@DiscriminatorValue(value = "WorkshopManagerFormFiller")
public class WorkshopManagerFormFiller extends FormFiller {

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id")
    private WorkshopManager workshopManager;

    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }

    public void setWorkshopManager(WorkshopManager workshopManager) {
        this.workshopManager = workshopManager;
    }
}
