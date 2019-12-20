package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FormApplicant;

import javax.persistence.*;

@Entity
@Table
@DiscriminatorValue(value = "GroupFormApplicant")

public class GroupFormApplicant extends FormApplicant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "workshop_group_id")
    private WorkshopGroup workshopGroup;


}
