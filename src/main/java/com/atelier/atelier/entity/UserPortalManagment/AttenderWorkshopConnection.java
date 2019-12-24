package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttender;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(value = "AttenderWorkshopConnection")
public class AttenderWorkshopConnection  extends WorkshopAttender{


    @OneToOne
    @JoinColumn(name = "attender_id", unique = true)
    private Attender attender;

    public AttenderWorkshopConnection() {
    }

    @JsonIgnore
    public Attender getAttender() {
        return attender;
    }

    public void setAttender(Attender attender) {
        this.attender = attender;
    }
}
