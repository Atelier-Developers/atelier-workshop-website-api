package com.atelier.atelier.entity.WorkshopManagment;


import com.atelier.atelier.entity.FormService.Form;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "WorkshopForm")
public class WorkshopForm extends Form {

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;


    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
