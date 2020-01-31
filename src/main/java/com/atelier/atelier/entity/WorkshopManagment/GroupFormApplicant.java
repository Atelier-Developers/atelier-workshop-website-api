package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GroupFormApplicant")
public class GroupFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    private WorkshopGroup workshopGroup;

    public WorkshopGroup getWorkshopGroup() {
        return workshopGroup;
    }

    public void setWorkshopGroup(WorkshopGroup workshopGroup) {
        this.workshopGroup = workshopGroup;
    }
}
