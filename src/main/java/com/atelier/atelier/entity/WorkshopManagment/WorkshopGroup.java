package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
public class WorkshopGroup {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "workshopGroup")
    private List<WorkshopGraderInfo> graderInfos;

    @OneToMany(mappedBy = "workshopGroup")
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "workshopGroup")
    private List<GroupFormApplicant> groupFormApplicants;
}
