package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.PaymentService.Payment;
import com.atelier.atelier.entity.RequestService.RequestData;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/*
    dar in form, form applicant AttenderFormApplicant hast va Answer ham Answer
 */

@Entity
@DiscriminatorValue(value = "AttenderRegisterForm")

public class AttenderRegisterForm extends Form implements RequestData {

    @OneToMany(mappedBy = "attenderRegisterForm")
    private List<AttenderPaymentTab> payments;

}
