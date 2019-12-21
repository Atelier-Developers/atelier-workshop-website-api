package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.RequestService.Requester;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "Grader")
public class Grader extends Requester implements Role {

    @OneToOne(mappedBy = "grader")
    private GraderWorkshopConnection graderWorkshopConnection;
}
