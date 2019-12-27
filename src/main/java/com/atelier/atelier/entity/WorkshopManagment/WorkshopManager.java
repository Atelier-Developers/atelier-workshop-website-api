package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_MANAGER_TYPE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class WorkshopManager {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopManager")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshopManager")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

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


    public void addFormFiller(WorkshopManagerFormFiller workshopManagerFormFiller){
        if (formFillerList == null){
            formFillerList = new ArrayList<>();
        }
        formFillerList.add(workshopManagerFormFiller);
    }
}
