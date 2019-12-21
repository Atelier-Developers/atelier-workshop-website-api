package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_MANAGER_TYPE")

public abstract class WorkshopManager {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopManager")
    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshopManager")
    private List<WorkshopManagerFormFiller> formFillerList;

}
