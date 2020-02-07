package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;
import com.atelier.atelier.entity.RequestService.Request;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "GraderFormApplicant")
public class GraderFormApplicant extends FormApplicant {

    @ManyToOne
    @JoinColumn(name = "workshop_grader_id")
    private WorkshopGrader workshopGrader;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public WorkshopGrader getWorkshopGrader() {
        return workshopGrader;
    }

    public void setWorkshopGrader(WorkshopGrader workshopGrader) {
        this.workshopGrader = workshopGrader;
    }
}
