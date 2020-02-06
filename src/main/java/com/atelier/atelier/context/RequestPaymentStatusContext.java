package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;

public class RequestPaymentStatusContext {

    private User user;
    private String requestStatus;
    private boolean paymentState;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isPaymentState() {
        return paymentState;
    }

    public void setPaymentState(boolean paymentState) {
        this.paymentState = paymentState;
    }
}
