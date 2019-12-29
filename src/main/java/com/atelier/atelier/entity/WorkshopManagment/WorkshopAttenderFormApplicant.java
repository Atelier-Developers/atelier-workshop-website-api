package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "WorkshopAttenderFormApplicant")
public class WorkshopAttenderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_attender_info_id", nullable = false)
    private WorkshopAttenderInfo workshopAttenderInfo;

    public WorkshopAttenderInfo getWorkshopAttenderInfo() {
        return workshopAttenderInfo;
    }

    public void setWorkshopAttenderInfo(WorkshopAttenderInfo workshopAttenderInfo) {
        this.workshopAttenderInfo = workshopAttenderInfo;
    }
}
