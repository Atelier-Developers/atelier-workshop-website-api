package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.UserPortalManagment.Attender;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Workshop {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OfferedWorkshop> offeredWorkshops;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public void addOfferingWorkshop(OfferedWorkshop offeredWorkshop){
        if(offeredWorkshops == null){
            offeredWorkshops = new ArrayList<>();
        }
        offeredWorkshops.add(offeredWorkshop);
    }

    public void addOfferingWorkshopRelation(OfferedWorkshopRelationDetail offeredWorkshopRelationDetail){
        if(offeredWorkshopRelationDetails == null){
            offeredWorkshopRelationDetails = new ArrayList<>();
        }
        offeredWorkshopRelationDetails.add(offeredWorkshopRelationDetail);
    }

}
