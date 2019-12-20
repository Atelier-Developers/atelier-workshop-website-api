package com.atelier.atelier.entity.WorkshopManagment;

import WorkshopSystem.FormService.Form;
import WorkshopSystem.PaymentService.Payment;
import WorkshopSystem.RequestService.RequestData;

import java.util.List;

/*
    dar in form, form applicant AttenderFormApplicant hast va Answer ham Answer
 */

public class AttenderRegisterForm extends Form implements RequestData {
    private List<AttenderPaymentTab> payments;
}
