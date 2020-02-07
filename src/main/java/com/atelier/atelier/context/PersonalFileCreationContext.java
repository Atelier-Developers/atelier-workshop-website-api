package com.atelier.atelier.context;

import java.util.List;

public class PersonalFileCreationContext {

    private String senderType;
    private List<String> receiverTypes;
    private String title;
    private String description;
    private String type;
    private String link;


    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public List<String> getReceiverTypes() {
        return receiverTypes;
    }

    public void setReceiverTypes(List<String> receiverTypes) {
        this.receiverTypes = receiverTypes;
    }

    //    public long getReceiverId() {
//        return receiverId;
//    }
//
//    public void setReceiverId(long receiverId) {
//        this.receiverId = receiverId;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
