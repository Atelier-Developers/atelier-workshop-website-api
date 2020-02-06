package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopFileReceiver;

import java.util.List;

public class WorkshopFileContext {

    private String title;
    private String description;
    private String type;
    private String link;
    private List<String> receiverList;


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


    public List<String> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }
}
