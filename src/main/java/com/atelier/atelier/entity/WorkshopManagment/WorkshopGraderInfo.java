package com.atelier.atelier.entity.WorkshopManagment;


import javax.persistence.*;
import java.util.List;
@Entity
@Table
public class WorkshopGraderInfo {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_grader_id", unique = true)
    private WorkshopGrader workshopGrader;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id", unique = true)
    private OfferedWorkshop offeredWorkshop;


    @ManyToOne
    @JoinColumn(name = "workshop_group_id", unique = true)
    private WorkshopGroup workshopGroup;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private List<WorkshopGraderFormFiller> formFillers;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private List<WorkshopGraderFormApplicant> workshopGraderFormApplicants;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private  List<GraderEvaluationForm> evaluationForms;


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

    public List<GraderEvaluationForm> getEvaluationForms() {
        return evaluationForms;
    }

    public void setEvaluationForms(List<GraderEvaluationForm> evaluationForms) {
        this.evaluationForms = evaluationForms;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
