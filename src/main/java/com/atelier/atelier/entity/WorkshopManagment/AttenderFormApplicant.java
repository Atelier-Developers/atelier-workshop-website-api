package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;
import com.atelier.atelier.entity.RequestService.Request;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "AttenderFormApplicant")
public class AttenderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_attender_id")
    private WorkshopAttender workshopAttender;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Request request;

    public WorkshopAttender getWorkshopAttender() {
        return workshopAttender;
    }

    public void setWorkshopAttender(WorkshopAttender workshopAttender) {
        this.workshopAttender = workshopAttender;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
