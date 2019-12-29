package com.atelier.atelier.context;

import java.util.List;

public class GroupWorkshopContext {

   private String name;
   private List<Long> gradersId;
   private List<Long> attenderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getGradersId() {
        return gradersId;
    }

    public void setGradersId(List<Long> gradersId) {
        this.gradersId = gradersId;
    }

    public List<Long> getAttenderId() {
        return attenderId;
    }

    public void setAttenderId(List<Long> attenderId) {
        this.attenderId = attenderId;
    }
}
