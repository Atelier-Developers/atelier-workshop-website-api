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
    @JoinColumn(name = "attender_request_payment_tab_id")
    private AttenderRequestPaymentTab attenderRequestPaymentTab;

}
