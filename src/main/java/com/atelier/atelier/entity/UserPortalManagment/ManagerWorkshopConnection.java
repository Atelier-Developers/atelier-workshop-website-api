package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopManager;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "ManagerWorkshopConnection" )
public class ManagerWorkshopConnection extends WorkshopManager implements Role {



}
