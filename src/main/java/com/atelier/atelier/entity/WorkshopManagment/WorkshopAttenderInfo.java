package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class WorkshopAttenderInfo {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id", unique = true)
    private OfferedWorkshop offeredWorkshop;

    @OneToMany(mappedBy = "workshopAttenderInfo")
    private List<WorkshopAttenderFormApplicant> workshopAttenderFormApplicants;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id", unique = true)
    private WorkshopAttender workshopAttender;

    @ManyToOne
    @JoinColumn(name = "workshop_group_id", unique = true)
    private WorkshopGroup workshopGroup;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    @JsonIgnore
    public List<WorkshopAttenderFormApplicant> getWorkshopAttenderFormApplicants() {
        return workshopAttenderFormApplicants;
    }

    public void setWorkshopAttenderFormApplicants(List<WorkshopAttenderFormApplicant> workshopAttenderFormApplicants) {
        this.workshopAttenderFormApplicants = workshopAttenderFormApplicants;
    }

    public WorkshopAttender getWorkshopAttender() {
        return workshopAttender;
    }

    public void setWorkshopAttender(WorkshopAttender workshopAttender) {
        this.workshopAttender = workshopAttender;
    }

    public WorkshopGroup getWorkshopGroup() {
        return workshopGroup;
    }

    public void setWorkshopGroup(WorkshopGroup workshopGroup) {
        this.workshopGroup = workshopGroup;
    }


    public void addWorkshopAttenderFormApplicant( WorkshopAttenderFormApplicant workshopAttenderFormApplicant){
        if ( workshopAttenderFormApplicants == null ){
            workshopAttenderFormApplicants = new ArrayList<WorkshopAttenderFormApplicant>();
        }

        workshopAttenderFormApplicants.add(workshopAttenderFormApplicant);
    }
}
