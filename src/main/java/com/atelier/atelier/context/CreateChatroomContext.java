package com.atelier.atelier.context;

import java.util.List;

public class CreateChatroomContext {

    private String name;
    private List<Long> userIds;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
