package com.atelier.atelier.context;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGraderInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupElementContext {

    private long groupId;
    private String groupName;
    private List<GroupMemberContext> graderInfos;
    private List<GroupMemberContext> attendeeInfos;


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
