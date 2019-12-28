package com.atelier.atelier.context;

import com.atelier.atelier.entity.RequestService.RequestState;

public class RequestStatusContext {

    private long requestId;
    private RequestState requestState;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }
}
