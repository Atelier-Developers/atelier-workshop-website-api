package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.Requester;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(value = "Attender")
public class Attender extends Requester implements Role{


    @OneToOne(mappedBy = "attender", cascade = CascadeType.ALL)
    private AttenderWorkshopConnection attenderWorkshopConnection;

    public Attender(){
    }

    public AttenderWorkshopConnection getAttenderWorkshopConnection() {
        return attenderWorkshopConnection;
    }

    public void setAttenderWorkshopConnection(AttenderWorkshopConnection attenderWorkshopConnection) {
        this.attenderWorkshopConnection = attenderWorkshopConnection;
    }
}
