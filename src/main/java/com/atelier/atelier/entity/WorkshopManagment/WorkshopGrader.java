package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "WORKSHOP_GRADER_TYPE")

public abstract class WorkshopGrader {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "workshopGrader")
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "workshopGrader")
    private List<GraderFormApplicant> graderFormApplicants;
}
