package com.atelier.atelier.entity.MessagingSystem;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CHATTER_TYPE")
@DiscriminatorOptions(force = true)
public abstract class Chatter {

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    @JoinTable(
            name = "chatroom_chatters",
            joinColumns = @JoinColumn(name = "chatter_id"),
            inverseJoinColumns = @JoinColumn(name = "chatroom_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Chatroom> chatrooms;


    @OneToMany(mappedBy = "chatter")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ChatterMessageRelation> chatterMessageRelations;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Chatroom> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(List<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public List<ChatterMessageRelation> getChatterMessageRelations() {
        return chatterMessageRelations;
    }

    public void setChatterMessageRelations(List<ChatterMessageRelation> chatterMessageRelations) {
        this.chatterMessageRelations = chatterMessageRelations;
    }

    public void addChatroom(Chatroom chatroom){
        if (chatrooms == null){
            chatrooms = new ArrayList<Chatroom>();
        }
        chatrooms.add(chatroom);
    }

    public void addChatterMessageRelation(ChatterMessageRelation chatterMessageRelation){
        if (chatterMessageRelations == null){
            chatterMessageRelations = new ArrayList<ChatterMessageRelation>();
        }
        chatterMessageRelations.add(chatterMessageRelation);
    }
}
