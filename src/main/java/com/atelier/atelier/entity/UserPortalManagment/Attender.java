package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.Requester;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(value = "Attender")
public class Attender extends Requester implements Role{


    @OneToOne(mappedBy = "attender")
    private AttenderWorkshopConnection attenderWorkshopConnection;


}
