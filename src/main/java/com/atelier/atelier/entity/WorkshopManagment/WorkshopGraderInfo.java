package com.atelier.atelier.entity.WorkshopManagment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class WorkshopGraderInfo {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_grader_id",  nullable = false)
    private WorkshopGrader workshopGrader;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id",  nullable = false)
    private OfferedWorkshop offeredWorkshop;


    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkshopGroup workshopGroup;

    @OneToMany(mappedBy = "workshopGraderInfo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private List<WorkshopGraderFormFiller> formFillers;

    @OneToMany(mappedBy = "workshopGraderInfo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopGraderFormApplicant> workshopGraderFormApplicants;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkshopGrader getWorkshopGrader() {
        return workshopGrader;
    }

    public void setWorkshopGrader(WorkshopGrader workshopGrader) {
        this.workshopGrader = workshopGrader;
    }

    public WorkshopGroup getWorkshopGroup() {
        return workshopGroup;
    }

    public void setWorkshopGroup(WorkshopGroup workshopGroup) {
        this.workshopGroup = workshopGroup;
    }


    public List<WorkshopGraderFormFiller> getFormFillers() {
        return formFillers;
    }

    public void setFormFillers(List<WorkshopGraderFormFiller> formFillers) {
        this.formFillers = formFillers;
    }


    public List<WorkshopGraderFormApplicant> getWorkshopGraderFormApplicants() {
        return workshopGraderFormApplicants;
    }

    public void setWorkshopGraderFormApplicants(List<WorkshopGraderFormApplicant> workshopGraderFormApplicants) {
        this.workshopGraderFormApplicants = workshopGraderFormApplicants;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof WorkshopGraderInfo)){
            return false;
        }
        WorkshopGraderInfo workshopGraderInfo = (WorkshopGraderInfo) obj;
        return this.id == workshopGraderInfo.id;
    }


    public void addWorkshopGraderFormApplicants(WorkshopGraderFormApplicant workshopGraderFormApplicant){
        if (workshopGraderFormApplicants == null){
            workshopGraderFormApplicants = new ArrayList<>();
        }

        workshopGraderFormApplicants.add(workshopGraderFormApplicant);
    }
}
