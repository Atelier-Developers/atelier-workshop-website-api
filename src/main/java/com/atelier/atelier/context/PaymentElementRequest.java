package com.atelier.atelier.context;

import java.math.BigDecimal;
import java.util.Calendar;

public class PaymentElementRequest {

    private String dueDate;
    private String amount;

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
