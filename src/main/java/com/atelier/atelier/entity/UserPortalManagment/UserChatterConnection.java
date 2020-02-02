package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.MessagingSystem.Chatter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "UserChatterConnection")
public class UserChatterConnection extends Chatter {

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;


    public UserChatterConnection() {
    }


    public UserChatterConnection(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
