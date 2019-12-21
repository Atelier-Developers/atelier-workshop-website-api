package com.atelier.atelier.entity.UserPortalManagment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class SystemAdmin implements Role {

    @Id
    @GeneratedValue
    private long id;

}
