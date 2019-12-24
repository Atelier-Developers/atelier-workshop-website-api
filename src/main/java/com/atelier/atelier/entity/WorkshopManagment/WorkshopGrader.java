package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_GRADER_TYPE")

public abstract class WorkshopGrader {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopGrader")
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "workshopGrader")
    private List<GraderFormApplicant> graderFormApplicants;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public List<WorkshopGraderInfo> getWorkshopGraderInfos() {
        return workshopGraderInfos;
    }

    public void setWorkshopGraderInfos(List<WorkshopGraderInfo> workshopGraderInfos) {
        this.workshopGraderInfos = workshopGraderInfos;
    }

    @JsonIgnore
    public List<GraderFormApplicant> getGraderFormApplicants() {
        return graderFormApplicants;
    }

    public void setGraderFormApplicants(List<GraderFormApplicant> graderFormApplicants) {
        this.graderFormApplicants = graderFormApplicants;
    }

    public void addGraderInfo(WorkshopGraderInfo workshopGraderInfo) {
        if (workshopGraderInfos == null) {
            workshopGraderInfos = new ArrayList<>();
        }
        workshopGraderInfos.add(workshopGraderInfo);
    }
}
