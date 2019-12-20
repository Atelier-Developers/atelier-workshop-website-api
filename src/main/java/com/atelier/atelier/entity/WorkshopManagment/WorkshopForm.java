package com.atelier.atelier.entity.WorkshopManagment;


/*
    dar in form, form applicant WorkshopAttenderFormApplicant ya GroupFormApplicant hast va Answer ham FilledAnswer va FormFiller ham
    WorkshopGraderFormFiller
 */

import com.atelier.atelier.entity.FormService.Form;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "WorkshopForm" )
public class WorkshopForm extends Form {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;


}
