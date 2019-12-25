package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_ATTENDER_TYPE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class WorkshopAttender {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopAttender")
    private List<WorkshopAttenderInfo> workshopAttenderInfos;

    @OneToMany(mappedBy = "workshopAttender")
    private List<AttenderFormApplicant> attenderFormApplicants;

    public WorkshopAttender() {
        this.workshopAttenderInfos = new ArrayList<>();
        this.attenderFormApplicants = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public List<WorkshopAttenderInfo> getWorkshopAttenderInfos() {
        return workshopAttenderInfos;
    }

    public void setWorkshopAttenderInfos(List<WorkshopAttenderInfo> workshopAttenderInfos) {
        this.workshopAttenderInfos = workshopAttenderInfos;
    }

    @JsonIgnore
    public List<AttenderFormApplicant> getAttenderFormApplicants() {
        return attenderFormApplicants;
    }

    public void setAttenderFormApplicants(List<AttenderFormApplicant> attenderFormApplicants) {
        this.attenderFormApplicants = attenderFormApplicants;
    }

    public void addWorkshopAttenderInfo(WorkshopAttenderInfo workshopAttenderInfo){
        this.workshopAttenderInfos.add(workshopAttenderInfo);
    }

    public void addAttenderFormApplicants(AttenderFormApplicant attenderFormApplicant){
        this.attenderFormApplicants.add(attenderFormApplicant);
    }
}
