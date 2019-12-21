package com.atelier.atelier.entity.PaymentService;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
public class CashPayment implements Payment {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private double value;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar paymentDate;
}
