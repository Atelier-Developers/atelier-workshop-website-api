package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;

@Entity
@Table
public class OfferedWorkshopRelationDetail {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;

    @ManyToOne
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @Enumerated(EnumType.ORDINAL)
    private DependencyType dependencyType;
}
