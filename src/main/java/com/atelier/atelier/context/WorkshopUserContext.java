package com.atelier.atelier.context;

import java.util.List;

public class WorkshopUserContext {

    private List<WorkshopUserElementContext> graderUsers;
    private List<WorkshopUserElementContext> attUsers;
    private List<WorkshopUserElementContext> managerUsers;

    public List<WorkshopUserElementContext> getGraderUsers() {
        return graderUsers;
    }

    public void setGraderUsers(List<WorkshopUserElementContext> graderUsers) {
        this.graderUsers = graderUsers;
    }

    public List<WorkshopUserElementContext> getManagerUsers() {
        return managerUsers;
    }

    public void setManagerUsers(List<WorkshopUserElementContext> managerUsers) {
        this.managerUsers = managerUsers;
    }

    public List<WorkshopUserElementContext> getAttUsers() {
        return attUsers;
    }

    public void setAttUsers(List<WorkshopUserElementContext> attUsers) {
        this.attUsers = attUsers;
    }
}
