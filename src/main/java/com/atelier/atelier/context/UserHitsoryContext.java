package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class UserHitsoryContext {

    private List<OfferedWorkshop> gradedOfferedWorkshops;
    private List<OfferedWorkshop> attendedOfferedWorkshops;
    private List<OfferedWorkshop> managedOfferedWorkshops;

    public List<OfferedWorkshop> getGradedOfferedWorkshops() {
        return gradedOfferedWorkshops;
    }

    public void setGradedOfferedWorkshops(List<OfferedWorkshop> gradedOfferedWorkshops) {
        this.gradedOfferedWorkshops = gradedOfferedWorkshops;
    }

    public List<OfferedWorkshop> getAttendedOfferedWorkshops() {
        return attendedOfferedWorkshops;
    }

    public void setAttendedOfferedWorkshops(List<OfferedWorkshop> attendedOfferedWorkshops) {
        this.attendedOfferedWorkshops = attendedOfferedWorkshops;
    }

    public List<OfferedWorkshop> getManagedOfferedWorkshops() {
        return managedOfferedWorkshops;
    }

    public void setManagedOfferedWorkshops(List<OfferedWorkshop> managedOfferedWorkshops) {
        this.managedOfferedWorkshops = managedOfferedWorkshops;
    }
}
