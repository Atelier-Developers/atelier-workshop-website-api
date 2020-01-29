package com.atelier.atelier.context;

import java.util.List;

public class EditGroupContext {
    private long groupId;
    private List<Long> gradersId;
    private List<Long> attendersId;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
