package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.Form;
import com.atelier.atelier.entity.RequestService.RequestData;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
    dar in form, form applicant GraderFormApplicant hast va Answer ham Answer
 */


@Entity
@Table
@DiscriminatorValue(value = "GraderRequestForm")
public class GraderRequestForm extends Form implements RequestData {

}
