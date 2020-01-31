package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.RequestService.Requester;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "Grader")
public class Grader extends Requester implements Role {

    @OneToOne(mappedBy = "grader", cascade = CascadeType.ALL)
    private GraderWorkshopConnection graderWorkshopConnection;

    public GraderWorkshopConnection getGraderWorkshopConnection() {
        return graderWorkshopConnection;
    }

    public void setGraderWorkshopConnection(GraderWorkshopConnection graderWorkshopConnection) {
        this.graderWorkshopConnection = graderWorkshopConnection;
    }
}
