package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.RequestService.Requestable;
import com.fasterxml.jackson.annotation.*;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopForm> workshopForms;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<OfferedWorkshopRelationDetail> workshopRelationDetails;

    @OneToOne(mappedBy = "offeredWorkshop", cascade = CascadeType.ALL)
    private GraderEvaluationForm graderEvaluationForm;


    @OneToOne
    @JoinColumn(name = "grader_request_form_id", unique = true)
    private GraderRequestForm graderRequestForm;

    @OneToOne
    @JoinColumn(name = "attender_register_form_id", unique = true)
    private AttenderRegisterForm attenderRegisterForm;

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


    public List<WorkshopForm> getWorkshopForms() {
        return workshopForms;
    }

    public void setWorkshopForms(List<WorkshopForm> workshopForms) {
        this.workshopForms = workshopForms;
    }

    public GraderEvaluationForm getGraderEvaluationForm() {
        return graderEvaluationForm;
    }

    public void setGraderEvaluationForm(GraderEvaluationForm graderEvaluationForm) {
        this.graderEvaluationForm = graderEvaluationForm;
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

    public void addWorkshopForm(WorkshopForm workshopForm){
        if ( workshopForms == null ){
            workshopForms = new ArrayList<>();
        }

        workshopForms.add(workshopForm);
    }

    public GraderRequestForm getGraderRequestForm() {
        return graderRequestForm;
    }

    public void setGraderRequestForm(GraderRequestForm graderRequestForm) {
        this.graderRequestForm = graderRequestForm;
    }

    public AttenderRegisterForm getAttenderRegisterForm() {
        return attenderRegisterForm;
    }

    public void setAttenderRegisterForm(AttenderRegisterForm attenderRegisterForm) {
        this.attenderRegisterForm = attenderRegisterForm;
    }
}
