package com.atelier.atelier.entity.WorkshopManagment;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class WorkshopManagerInfo {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id",  nullable = false)
    private WorkshopManager workshopManager;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id",  nullable = false)
    private OfferedWorkshop offeredWorkshop;

    public WorkshopManagerInfo() {
    }


    public WorkshopManagerInfo(WorkshopManager workshopManager, OfferedWorkshop offeredWorkshop) {
        this.workshopManager = workshopManager;
        this.offeredWorkshop = offeredWorkshop;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }

    public void setWorkshopManager(WorkshopManager workshopManager) {
        this.workshopManager = workshopManager;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

}
