package com.atelier.atelier.context;

import com.atelier.atelier.entity.MessagingSystem.Chatroom;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshopChatroom;

import java.util.List;

public class OfferedWorkshopChatroomContext {

    private List<OfferedWorkshopChatroom> offeredWorkshopChatrooms;

    public List<OfferedWorkshopChatroom> getOfferedWorkshopChatrooms() {
        return offeredWorkshopChatrooms;
    }

    public void setOfferedWorkshopChatrooms(List<OfferedWorkshopChatroom> offeredWorkshopChatrooms) {
        this.offeredWorkshopChatrooms = offeredWorkshopChatrooms;
    }
}
