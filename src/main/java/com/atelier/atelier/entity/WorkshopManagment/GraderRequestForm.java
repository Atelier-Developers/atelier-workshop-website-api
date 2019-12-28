package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.RequestService.RequestData;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/*
    dar in form, form applicant GraderFormApplicant hast va Answer ham Answer
 */


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
