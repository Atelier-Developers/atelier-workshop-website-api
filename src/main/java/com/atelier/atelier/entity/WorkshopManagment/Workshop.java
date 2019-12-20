package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table

public class Workshop {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "workshop")
    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshop")
    private List<OfferedWorkshopRelationDetail> offeredWorkshopRelationDetails;
}
