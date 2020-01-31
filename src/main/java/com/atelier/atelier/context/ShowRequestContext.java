package com.atelier.atelier.context;

import com.atelier.atelier.entity.RequestService.RequestState;

public class ShowRequestContext {
    private long id;
    private RequestState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }
}
