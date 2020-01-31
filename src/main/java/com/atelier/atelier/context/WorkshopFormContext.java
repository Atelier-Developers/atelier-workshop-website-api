package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopForm;

public class WorkshopFormContext {

    private WorkshopForm workshopForm;
    private long offeredWorkshopId;

    public WorkshopForm getWorkshopForm() {
        return workshopForm;
    }

    public void setWorkshopForm(WorkshopForm workshopForm) {
        this.workshopForm = workshopForm;
    }

    public long getOfferedWorkshopId() {
        return offeredWorkshopId;
    }

    public void setOfferedWorkshopId(long offeredWorkshopId) {
        this.offeredWorkshopId = offeredWorkshopId;
    }
}
