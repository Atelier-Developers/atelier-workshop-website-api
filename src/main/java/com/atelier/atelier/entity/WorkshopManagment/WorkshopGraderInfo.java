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
    @JoinColumn(name = "workshop_grader_id")
    private WorkshopGrader workshopGrader;

    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    private WorkshopGroup workshopGroup;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private List<WorkshopGraderFormFiller> formFillers;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private List<WorkshopGraderFormApplicant> workshopGraderFormApplicants;

    @OneToMany(mappedBy = "workshopGraderInfo")
    private  List<GraderEvaluationForm> evaluationForms;
}
