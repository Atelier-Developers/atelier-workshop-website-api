package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/*
    dar in form, form applicant GraderFormApplicant hast va Answer ham FilledAnswer va FormFiller ham
    WorkshopManagerFormFiller
 */

public class GraderEvaluationForm extends Form {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_grader_info_id")
    private WorkshopGraderInfo workshopGraderInfo;
}
