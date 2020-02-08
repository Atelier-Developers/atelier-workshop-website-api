package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.PaymentService.Payment;
import com.atelier.atelier.entity.UserPortalManagment.File;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JoinColumn(name = "attender_request_payment_tab_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AttenderRequestPaymentTab attenderRequestPaymentTab;

    @Column(nullable = false)
    private BigDecimal value;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar paymentDate;

    @Column(nullable = false)
    private boolean isPaid;

    @Column
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private File file;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

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
