package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class UserHitsoryContext {

    private List<OfferedWorkshopManagerNameContext> managedWorkshops;
    private List<OfferedWorkshopManagerNameContext> attendedWorkshops;
    private List<OfferedWorkshopManagerNameContext> gradedWorkshops;

    public List<OfferedWorkshopManagerNameContext> getManagedWorkshops() {
        return managedWorkshops;
    }

    public void setManagedWorkshops(List<OfferedWorkshopManagerNameContext> managedWorkshops) {
        this.managedWorkshops = managedWorkshops;
    }

    public List<OfferedWorkshopManagerNameContext> getAttendedWorkshops() {
        return attendedWorkshops;
    }

    public void setAttendedWorkshops(List<OfferedWorkshopManagerNameContext> attendedWorkshops) {
        this.attendedWorkshops = attendedWorkshops;
    }

    public List<OfferedWorkshopManagerNameContext> getGradedWorkshops() {
        return gradedWorkshops;
    }

    public void setGradedWorkshops(List<OfferedWorkshopManagerNameContext> gradedWorkshops) {
        this.gradedWorkshops = gradedWorkshops;
    }
}
