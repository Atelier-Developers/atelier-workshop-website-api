package com.atelier.atelier.context;

import java.util.List;

public class GroupWorkshopContext {

   private String name;
   private List<Long> gradersId;
   private List<Long> attendersId;

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

    public List<Long> getAttendersId() {
        return attendersId;
    }

    public void setAttendersId(List<Long> attendersId) {
        this.attendersId = attendersId;
    }
}
