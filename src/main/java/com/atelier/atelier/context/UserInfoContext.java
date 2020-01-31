package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;

public class UserInfoContext {

    private User user;
    private long infoId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }
}
