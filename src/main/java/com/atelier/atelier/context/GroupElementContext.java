package com.atelier.atelier.context;

import java.util.ArrayList;
import java.util.List;

public class GroupElementContext {

    private long id;
    private String name;
    private List<GroupMemberContext> graderInfos;
    private List<GroupMemberContext> attendeeInfos;


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

    public List<GroupMemberContext> getGraderInfos() {
        return graderInfos;
    }

    public void setGraderInfos(List<GroupMemberContext> graderInfos) {
        this.graderInfos = graderInfos;
    }

    public List<GroupMemberContext> getAttendeeInfos() {
        return attendeeInfos;
    }

    public void setAttendeeInfos(List<GroupMemberContext> attendeeInfos) {
        this.attendeeInfos = attendeeInfos;
    }

    public void addGraderInfo(GroupMemberContext groupMemberContext){
        if (graderInfos == null ){
            graderInfos = new ArrayList<>();
        }
        graderInfos.add(groupMemberContext);
    }

    public void addAttendeeInfo(GroupMemberContext groupMemberContext){
        if ( attendeeInfos == null ){
            attendeeInfos = new ArrayList<>();
        }
        attendeeInfos.add(groupMemberContext);
    }
}
