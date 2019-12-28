package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.PaymentService.Payment;
import com.atelier.atelier.entity.RequestService.RequestData;

import javax.persistence.*;
import java.util.List;

/*
    dar in form, form applicant AttenderFormApplicant hast va Answer ham Answer
 */

@Entity
@DiscriminatorValue(value = "AttenderRegisterForm")

public class AttenderRegisterForm extends Form implements RequestData {

    @OneToOne(mappedBy = "attenderRegisterForm")
    private OfferedWorkshop offeredWorkshop;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
