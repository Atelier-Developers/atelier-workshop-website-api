package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class WorkshopAttenderInfo {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;

    @OneToMany(mappedBy = "workshopAttenderInfo")
    private List<WorkshopAttenderFormApplicant> workshopAttenderFormApplicants;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id")
    private WorkshopAttender workshopAttender;

    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    private WorkshopGroup workshopGroup;
}
