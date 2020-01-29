package com.atelier.atelier.entity.WorkshopManagment;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table
public class WorkshopGroup {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "workshopGroup")
    private List<WorkshopGraderInfo> graderInfos;

    @OneToMany(mappedBy = "workshopGroup")
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "workshopGroup")
    private List<GroupFormApplicant> groupFormApplicants;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkshopGraderInfo> getGraderInfos() {
        return graderInfos;
    }

    public void setGraderInfos(List<WorkshopGraderInfo> graderInfos) {
        this.graderInfos = graderInfos;
    }

    public List<WorkshopAttenderInfo> getAttenderInfos() {
        return attenderInfos;
    }

    public void setAttenderInfos(List<WorkshopAttenderInfo> attenderInfos) {
        this.attenderInfos = attenderInfos;
    }

    public List<GroupFormApplicant> getGroupFormApplicants() {
        return groupFormApplicants;
    }

    public void setGroupFormApplicants(List<GroupFormApplicant> groupFormApplicants) {
        this.groupFormApplicants = groupFormApplicants;
    }

    public void addGrader(WorkshopGraderInfo workshopGraderInfo) {
        if (graderInfos == null) {
            graderInfos = new ArrayList<>();
        }
        graderInfos.add(workshopGraderInfo);
    }

    public void addAttender(WorkshopAttenderInfo workshopAttenderInfo){
        if(attenderInfos == null){
            attenderInfos = new ArrayList<>();
        }
        attenderInfos.add(workshopAttenderInfo);
    }

}
