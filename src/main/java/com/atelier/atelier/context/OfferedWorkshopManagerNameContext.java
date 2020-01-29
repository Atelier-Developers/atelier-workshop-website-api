package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class OfferedWorkshopManagerNameContext {

    private OfferedWorkshop offeredWorkshop;
    private List<String> workshopManagers;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }


    public List<String> getWorkshopManagers() {
        return workshopManagers;
    }

    public void setWorkshopManagers(List<String> workshopManagers) {
        this.workshopManagers = workshopManagers;
    }
}
