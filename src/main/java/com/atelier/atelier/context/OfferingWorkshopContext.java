package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;

import java.util.List;

public class OfferingWorkshopContext {
    private List<Long> prerequsiteId;
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

    public List<Long> getPrerequsiteId() {
        return prerequsiteId;
    }

    public void setPrerequsiteId(List<Long> prerequsiteId) {
        this.prerequsiteId = prerequsiteId;
    }
}
