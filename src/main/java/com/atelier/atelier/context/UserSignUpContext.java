package com.atelier.atelier.context;

import com.atelier.atelier.entity.UserPortalManagment.User;

public class UserSignUpContext {

    private User user;
    private String gender;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
