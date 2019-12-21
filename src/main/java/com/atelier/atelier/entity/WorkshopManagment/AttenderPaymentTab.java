package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.PaymentService.Payment;

import javax.persistence.*;

@Entity
@Table
public class AttenderPaymentTab implements Payment {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "attender_register_form_id")
    private AttenderRegisterForm attenderRegisterForm;

}
