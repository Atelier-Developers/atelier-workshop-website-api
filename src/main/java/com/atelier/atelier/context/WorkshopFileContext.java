package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopFileReceiver;

import java.util.List;

public class WorkshopFileContext {

    private String title;
    private String description;
    private List<String> receiverList;


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
