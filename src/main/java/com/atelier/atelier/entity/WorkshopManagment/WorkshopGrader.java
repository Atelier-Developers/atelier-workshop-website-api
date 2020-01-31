package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_GRADER_TYPE")
@DiscriminatorOptions(force = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class  WorkshopGrader {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopGrader")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "workshopGrader")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<GraderFormApplicant> graderFormApplicants;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<WorkshopGraderInfo> getWorkshopGraderInfos() {
        return workshopGraderInfos;
    }

    public void setWorkshopGraderInfos(List<WorkshopGraderInfo> workshopGraderInfos) {
        this.workshopGraderInfos = workshopGraderInfos;
    }


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
    public WorkshopGraderInfo getWorkshopGraderInfoOfferedWorkshop(OfferedWorkshop offeredWorkshop){
        for(WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos){
            if(workshopGraderInfo.getOfferedWorkshop().getId() == offeredWorkshop.getId()){
                return workshopGraderInfo;
            }
        }
        return null;

    }


    public void addGraderFormApplicant(GraderFormApplicant graderFormApplicant){
        if (graderFormApplicants == null){
            graderFormApplicants = new ArrayList<>();
        }

        graderFormApplicants.add(graderFormApplicant);
    }
}
