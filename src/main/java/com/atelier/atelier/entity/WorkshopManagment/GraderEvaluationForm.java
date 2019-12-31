package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

/*
    dar in form, form applicant GraderFormApplicant hast va Answer ham FilledAnswer va FormFiller ham
    WorkshopManagerFormFiller
 */

@Entity
@DiscriminatorValue(value = "GraderEvaluationForm")
public class GraderEvaluationForm extends Form {


    @OneToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
