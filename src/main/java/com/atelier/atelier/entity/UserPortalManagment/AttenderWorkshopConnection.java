package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttender;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttenderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "AttenderWorkshopConnection")
public class AttenderWorkshopConnection  extends WorkshopAttender{


    @OneToOne
    @JoinColumn(name = "attender_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Attender attender;

    public AttenderWorkshopConnection() {
    }

    public Attender getAttender() {
        return attender;
    }

    public void setAttender(Attender attender) {
        this.attender = attender;
    }

    public boolean hasPassedWorkshop(Workshop workshop){
        for(WorkshopAttenderInfo workshopAttenderInfo: getWorkshopAttenderInfos()){
            if(workshop.getId() == workshopAttenderInfo.getOfferedWorkshop().getWorkshop().getId()){
                return true;
            }
        }
        return false;
    }



}
