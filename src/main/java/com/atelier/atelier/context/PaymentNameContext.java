package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.AttenderPaymentTab;

import java.util.List;

public class PaymentNameContext {

    private String name;
    private List<AttenderPaymentTab> pays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttenderPaymentTab> getPays() {
        return pays;
    }

    public void setPays(List<AttenderPaymentTab> pays) {
        this.pays = pays;
    }
}
