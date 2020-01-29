package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_MANAGER_TYPE")
@DiscriminatorOptions(force = true)
public abstract class WorkshopManager {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopManager")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopManagerInfo> workshopManagerInfos;

    @OneToMany(mappedBy = "workshopManager")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopManagerFormFiller> formFillerList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<WorkshopManagerInfo> getWorkshopManagerInfos() {
        return workshopManagerInfos;
    }

    public void setWorkshopManagerInfos(List<WorkshopManagerInfo> workshopManagerInfos) {
        this.workshopManagerInfos = workshopManagerInfos;
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

    public void addWorkshopManagerInfo(WorkshopManagerInfo workshopManagerInfo){
        if (workshopManagerInfos == null ){
            workshopManagerInfos = new ArrayList<>();
        }
        workshopManagerInfos.add(workshopManagerInfo);
    }
}
