package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;

import java.util.List;

public class GroupUsersContext {

    private long groupId;
    private String groupName;
    private List<User> graders;
    private List<User> attendees;


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getGraders() {
        return graders;
    }

    public void setGraders(List<User> graders) {
        this.graders = graders;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }
}
