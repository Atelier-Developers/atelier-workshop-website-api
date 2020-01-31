package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class AttendeePageWorkshopsContext {

    private List<OfferedWorkshop> currentWorkshops;
    private List<OfferedWorkshop> soonToBeHeld;
    private List<OfferedWorkshop> passed;


    public List<OfferedWorkshop> getCurrentWorkshops() {
        return currentWorkshops;
    }

    public void setCurrentWorkshops(List<OfferedWorkshop> currentWorkshops) {
        this.currentWorkshops = currentWorkshops;
    }

    public List<OfferedWorkshop> getSoonToBeHeld() {
        return soonToBeHeld;
    }

    public void setSoonToBeHeld(List<OfferedWorkshop> soonToBeHeld) {
        this.soonToBeHeld = soonToBeHeld;
    }

    public List<OfferedWorkshop> getPassed() {
        return passed;
    }

    public void setPassed(List<OfferedWorkshop> passed) {
        this.passed = passed;
    }

}
