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
    @JoinColumn(name = "attender_request_payment_tab_id", unique = true, nullable = false)
    private AttenderRequestPaymentTab attenderRequestPaymentTab;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AttenderRequestPaymentTab getAttenderRequestPaymentTab() {
        return attenderRequestPaymentTab;
    }

    public void setAttenderRequestPaymentTab(AttenderRequestPaymentTab attenderRequestPaymentTab) {
        this.attenderRequestPaymentTab = attenderRequestPaymentTab;
    }
}
