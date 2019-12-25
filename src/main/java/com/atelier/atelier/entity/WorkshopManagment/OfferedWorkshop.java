package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.RequestService.Requestable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@DiscriminatorValue(value = "OfferedWorkshop")
public class OfferedWorkshop extends Requestable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    private String name;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @ManyToOne
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id")
    private WorkshopManager workshopManager;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopForm> workshopForms;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<OfferedWorkshopRelationDetail> workshopRelationDetails;

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }



    public Workshop getWorkshop() {
        return workshop;
    }

    public List<WorkshopGraderInfo> getWorkshopGraderInfos() {
        return workshopGraderInfos;
    }

    public void setWorkshopGraderInfos(List<WorkshopGraderInfo> workshopGraderInfos) {
        this.workshopGraderInfos = workshopGraderInfos;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }


    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }

    public void setWorkshopManager(WorkshopManager workshopManager) {
        this.workshopManager = workshopManager;
    }


    @JsonIgnore
    public List<WorkshopForm> getWorkshopForms() {
        return workshopForms;
    }

    public void setWorkshopForms(List<WorkshopForm> workshopForms) {
        this.workshopForms = workshopForms;
    }



    public List<WorkshopAttenderInfo> getAttenderInfos() {
        return attenderInfos;
    }

    public void setAttenderInfos(List<WorkshopAttenderInfo> attenderInfos) {
        this.attenderInfos = attenderInfos;
    }


    public List<OfferedWorkshopRelationDetail> getWorkshopRelationDetails() {
        return workshopRelationDetails;
    }

    public void setWorkshopRelationDetails(List<OfferedWorkshopRelationDetail> workshopRelationDetails) {
        this.workshopRelationDetails = workshopRelationDetails;
    }

    public void addWorkshopAttenderInfo(WorkshopAttenderInfo workshopAttenderInfo){
        if ( attenderInfos == null ){
            attenderInfos = new ArrayList<WorkshopAttenderInfo>();
        }

        attenderInfos.add(workshopAttenderInfo);
    }

    public void addWorkshopGraderrInfo(WorkshopGraderInfo workshopGraderInfo){
        if ( workshopGraderInfos == null ){
            workshopGraderInfos = new ArrayList<>();
        }

        workshopGraderInfos.add(workshopGraderInfo);
    }
}
