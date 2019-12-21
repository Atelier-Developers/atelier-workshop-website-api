package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttender;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@DiscriminatorValue(value = "AttenderWorkshopConnection")
public class AttenderWorkshopConnection  extends WorkshopAttender{


    @OneToOne
    @JoinColumn(name = "attender_id")
    private Attender attender;


}
