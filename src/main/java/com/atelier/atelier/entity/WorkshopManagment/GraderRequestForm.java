package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.RequestService.RequestData;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "GraderRequestForm")
public class GraderRequestForm extends Form implements RequestData {

    @OneToOne(mappedBy = "graderRequestForm")
    private OfferedWorkshop offeredWorkshop;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
