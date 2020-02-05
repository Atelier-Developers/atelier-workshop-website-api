package com.atelier.atelier.entity.WorkshopManagment;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
public class OfferedWorkshopInstallment {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OfferedWorkshop offeredWorkshop;


    @Column(nullable = false)
    private BigDecimal value;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar paymentDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
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
