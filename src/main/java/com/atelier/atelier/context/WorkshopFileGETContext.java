package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class WorkshopFileGETContext {

    private String title;
    private String description;
    private List<String> receivers;
    private String downloadURI;


    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
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

    public String getDownloadURI() {
        return downloadURI;
    }

    public void setDownloadURI(String downloadURI) {
        this.downloadURI = downloadURI;
    }

}
