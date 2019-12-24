package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_MANAGER_TYPE")

public abstract class WorkshopManager {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopManager")
    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshopManager")
    private List<WorkshopManagerFormFiller> formFillerList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<OfferedWorkshop> getOfferedWorkshops() {
        return offeredWorkshops;
    }

    public void setOfferedWorkshops(List<OfferedWorkshop> offeredWorkshops) {
        this.offeredWorkshops = offeredWorkshops;
    }

    public List<WorkshopManagerFormFiller> getFormFillerList() {
        return formFillerList;
    }

    public void setFormFillerList(List<WorkshopManagerFormFiller> formFillerList) {
        this.formFillerList = formFillerList;
    }
}
