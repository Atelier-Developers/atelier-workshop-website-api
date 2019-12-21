package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;

import javax.persistence.*;
import java.util.List;

/*
    dar in form, form applicant GraderFormApplicant hast va Answer ham FilledAnswer va FormFiller ham
    WorkshopManagerFormFiller
 */

@Entity
@DiscriminatorValue(value = "GraderEvaluationForm")
public class GraderEvaluationForm extends Form {

    @ManyToOne
    @JoinColumn(name = "workshop_grader_info_id")
    private WorkshopGraderInfo workshopGraderInfo;
}
