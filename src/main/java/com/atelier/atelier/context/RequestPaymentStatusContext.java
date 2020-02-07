package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;

public class RequestPaymentStatusContext {

    private User user;
    private String requestStatus;
    private boolean paymentState;
    private long id;


    public User getUser() {
        return user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
