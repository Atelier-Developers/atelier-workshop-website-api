package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderEvaluationForm")
public class GraderEvaluationForm extends Form {

    @OneToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
