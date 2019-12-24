package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

public class OfferingWorkshopContext {
    private OfferedWorkshop offeredWorkshop;
    private long workshopId;

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
}
