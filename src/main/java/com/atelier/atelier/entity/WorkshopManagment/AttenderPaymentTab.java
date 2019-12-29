package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.PaymentService.Payment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table
public class AttenderPaymentTab implements Payment {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "attender_request_payment_tab_id", unique = true, nullable = false)
    private AttenderRequestPaymentTab attenderRequestPaymentTab;

    @Column(nullable = false)
    private BigDecimal value;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar paymentDate;

    @Column(nullable = false)
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }
}
