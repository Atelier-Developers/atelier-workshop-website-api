package com.atelier.atelier.context;

public class OfferingWorkshopChatroomsIdContext {

    private long attendeeChatroomId;
    private long graderChatroomId;

    public long getAttendeeChatroomId() {
        return attendeeChatroomId;
    }

    public void setAttendeeChatroomId(long attendeeChatroomId) {
        this.attendeeChatroomId = attendeeChatroomId;
    }

    public long getGraderChatroomId() {
        return graderChatroomId;
    }

    public void setGraderChatroomId(long graderChatroomId) {
        this.graderChatroomId = graderChatroomId;
    }
}
