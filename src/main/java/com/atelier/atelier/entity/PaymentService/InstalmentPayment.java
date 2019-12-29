package com.atelier.atelier.entity.PaymentService;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table
public class InstalmentPayment implements Payment {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private double value;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dueDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }
}
