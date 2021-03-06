package com.atelier.atelier.entity.WorkshopManagment;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorkshopAttenderInfo {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id", nullable = false)
    private OfferedWorkshop offeredWorkshop;

    @OneToMany(mappedBy = "workshopAttenderInfo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopAttenderFormApplicant> workshopAttenderFormApplicants;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id", nullable = false)
    private WorkshopAttender workshopAttender;

    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkshopGroup workshopGroup;

    @OneToMany(mappedBy = "workshopAttenderInfo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PersonalFile> personalFiles;

    @Enumerated(EnumType.ORDINAL)
    private AttendeeStatusType attendeeStatusType;

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

    public List<PersonalFile> getPersonalFiles() {
        return personalFiles;
    }

    public void setPersonalFiles(List<PersonalFile> personalFiles) {
        this.personalFiles = personalFiles;
    }

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


    public AttendeeStatusType getAttendeeStatusType() {
        return attendeeStatusType;
    }

    public void setAttendeeStatusType(AttendeeStatusType attendeeStatusType) {
        this.attendeeStatusType = attendeeStatusType;
    }

    public void addWorkshopAttenderFormApplicant(WorkshopAttenderFormApplicant workshopAttenderFormApplicant){
        if ( workshopAttenderFormApplicants == null ){
            workshopAttenderFormApplicants = new ArrayList<WorkshopAttenderFormApplicant>();
        }

        workshopAttenderFormApplicants.add(workshopAttenderFormApplicant);
    }


}
