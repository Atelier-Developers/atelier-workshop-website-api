package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.RequestService.Requestable;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table
@DiscriminatorValue(value = "OfferedWorkshop")

public class OfferedWorkshop extends Requestable {

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @ManyToOne
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id")
    private WorkshopManager workshopManager;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopForm> workshopForms;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    private List<OfferedWorkshopRelationDetail> workshopRelationDetails;
}
