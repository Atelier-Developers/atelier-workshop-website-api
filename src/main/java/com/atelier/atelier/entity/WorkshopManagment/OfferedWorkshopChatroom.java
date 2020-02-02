package com.atelier.atelier.entity.WorkshopManagment;


import com.atelier.atelier.entity.MessagingSystem.Chatroom;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "OfferedWorkshopChatroom")
public class OfferedWorkshopChatroom extends Chatroom {


    @ManyToOne
    @JoinColumn(name = "offered_workshop_id")
    private OfferedWorkshop offeredWorkshop;


    public OfferedWorkshopChatroom() {
    }


    public OfferedWorkshopChatroom(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }


    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }
}
