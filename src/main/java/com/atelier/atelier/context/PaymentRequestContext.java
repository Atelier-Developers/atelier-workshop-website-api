package com.atelier.atelier.context;

import java.util.List;

public class PaymentRequestContext {

    private String type;
    private List<PaymentElementRequest> payments;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PaymentElementRequest> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentElementRequest> payments) {
        this.payments = payments;
    }
}
