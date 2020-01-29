package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;

import java.util.List;

public class OfferedWorkshopUserListsContext {

    private OfferedWorkshop offeredWorkshop;
    private List<User> workshopManagerUser;
    private List<User> attendeeUsers;
    private List<User> graderUsers;
    private List<String> preRequisites;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    public List<User> getWorkshopManagerUser() {
        return workshopManagerUser;
    }

    public void setWorkshopManagerUser(List<User> workshopManagerUser) {
        this.workshopManagerUser = workshopManagerUser;
    }

    public List<User> getAttendeeUsers() {
        return attendeeUsers;
    }

    public void setAttendeeUsers(List<User> attendeeUsers) {
        this.attendeeUsers = attendeeUsers;
    }

    public List<User> getGraderUsers() {
        return graderUsers;
    }

    public void setGraderUsers(List<User> graderUsers) {
        this.graderUsers = graderUsers;
    }

    public List<String> getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(List<String> preRequisites) {
        this.preRequisites = preRequisites;
    }
}
