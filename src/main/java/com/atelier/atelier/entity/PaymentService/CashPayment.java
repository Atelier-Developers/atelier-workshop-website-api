package com.atelier.atelier.entity.PaymentService;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table
public class CashPayment implements Payment {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private BigDecimal value;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar paymentDate;

    @Column(nullable = false)
    private boolean isPaid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
