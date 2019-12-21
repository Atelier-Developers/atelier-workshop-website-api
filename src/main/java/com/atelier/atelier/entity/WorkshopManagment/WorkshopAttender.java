package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_ATTENDER_TYPE")

public abstract class WorkshopAttender {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopAttender")
    private List<WorkshopAttenderInfo> workshopAttenderInfos;

    @OneToMany(mappedBy = "workshopAttender")
    private List<AttenderFormApplicant> attenderFormApplicants;
}
