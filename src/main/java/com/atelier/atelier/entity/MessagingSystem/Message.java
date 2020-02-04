package com.atelier.atelier.entity.MessagingSystem;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String text;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ChatterMessageRelation> chatterMessageRelations;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    public Message() {
    }


    public Message(List<ChatterMessageRelation> chatterMessageRelations, Chatroom chatroom) {
        this.chatterMessageRelations = chatterMessageRelations;
        this.chatroom = chatroom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ChatterMessageRelation> getChatterMessageRelations() {
        return chatterMessageRelations;
    }

    public void setChatterMessageRelations(List<ChatterMessageRelation> chatterMessageRelations) {
        this.chatterMessageRelations = chatterMessageRelations;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public void addChatterMessageRelation(ChatterMessageRelation chatterMessageRelation){
        if (chatterMessageRelations == null){
            chatterMessageRelations = new ArrayList<>();
        }
        chatterMessageRelations.add(chatterMessageRelation);
    }
}
