package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.PaymentService.Payment;
import com.atelier.atelier.entity.RequestService.RequestData;

import java.util.List;

/*
    dar in form, form applicant AttenderFormApplicant hast va Answer ham Answer
 */

public class AttenderRegisterForm extends Form implements RequestData {
    private List<AttenderPaymentTab> payments;
}
