package com.atelier.atelier.entity.MessagingSystem;

import javax.persistence.*;

@Entity
public class ChatterMessageRelation {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Enumerated(EnumType.ORDINAL)
    private MessageReadStatus messageReadStatus;

    @Enumerated(EnumType.ORDINAL)
    private MessageRelation messageRelation;

    @ManyToOne
    @JoinColumn(name = "chatter_id")
    private Chatter chatter;

    public ChatterMessageRelation() {
    }


    public ChatterMessageRelation(Message message, Chatter chatter) {
        this.message = message;
        this.chatter = chatter;
    }

    public MessageReadStatus getMessageReadStatus() {
        return messageReadStatus;
    }

    public void setMessageReadStatus(MessageReadStatus messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    public MessageRelation getMessageRelation() {
        return messageRelation;
    }

    public void setMessageRelation(MessageRelation messageRelation) {
        this.messageRelation = messageRelation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Chatter getChatter() {
        return chatter;
    }

    public void setChatter(Chatter chatter) {
        this.chatter = chatter;
    }
}
