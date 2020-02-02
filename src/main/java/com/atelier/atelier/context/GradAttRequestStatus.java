package com.atelier.atelier.context;

import com.atelier.atelier.entity.RequestService.Request;

public class GradAttRequestStatus {
    private Request attReq;
    private Request graderReq;

    public Request getAttReq() {
        return attReq;
    }

    public void setAttReq(Request attReq) {
        this.attReq = attReq;
    }

    public Request getGraderReq() {
        return graderReq;
    }

    public void setGraderReq(Request graderReq) {
        this.graderReq = graderReq;
    }
}
