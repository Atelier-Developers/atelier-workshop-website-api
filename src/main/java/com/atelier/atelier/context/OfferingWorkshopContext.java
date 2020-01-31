package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class OfferingWorkshopContext {
    private List<Long> preRequisiteId;
    private String startTime;
    private String endTime;
    private OfferedWorkshop offeredWorkshop;
    private long workshopId;
    private List<Long> userManagerId;


    public List<Long> getUserManagerId() {
        return userManagerId;
    }

    public void setUserManagerId(List<Long> userManagerId) {
        this.userManagerId = userManagerId;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    public long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(long workshopId) {
        this.workshopId = workshopId;
    }

    public List<Long> getPreRequisiteId() {
        return preRequisiteId;
    }

    public void setPreRequisiteId(List<Long> preRequisiteId) {
        this.preRequisiteId = preRequisiteId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
