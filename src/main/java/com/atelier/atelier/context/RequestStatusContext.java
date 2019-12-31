package com.atelier.atelier.context;

import com.atelier.atelier.entity.RequestService.RequestState;

public class RequestStatusContext {

    private long requestId;
    private String requestState;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }
}
