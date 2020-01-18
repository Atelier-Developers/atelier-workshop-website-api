package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

public class OfferedWorkshopManagerNameContext {

    private OfferedWorkshop offeredWorkshop;
    private String workshopManagerName;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    public String getWorkshopManagerName() {
        return workshopManagerName;
    }

    public void setWorkshopManagerName(String workshopManagerName) {
        this.workshopManagerName = workshopManagerName;
    }
}
