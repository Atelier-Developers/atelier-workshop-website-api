package com.atelier.atelier.entity.WorkshopManagment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table

public class Workshop {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "workshop")
    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshop")
    private List<OfferedWorkshopRelationDetail> offeredWorkshopRelationDetails;

    public Workshop(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OfferedWorkshop> getOfferedWorkshops() {
        return offeredWorkshops;
    }

    public void setOfferedWorkshops(List<OfferedWorkshop> offeredWorkshops) {
        this.offeredWorkshops = offeredWorkshops;
    }

    public List<OfferedWorkshopRelationDetail> getOfferedWorkshopRelationDetails() {
        return offeredWorkshopRelationDetails;
    }

    public void setOfferedWorkshopRelationDetails(List<OfferedWorkshopRelationDetail> offeredWorkshopRelationDetails) {
        this.offeredWorkshopRelationDetails = offeredWorkshopRelationDetails;
    }
}
