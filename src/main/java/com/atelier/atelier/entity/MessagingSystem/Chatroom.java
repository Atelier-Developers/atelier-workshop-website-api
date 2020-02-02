package com.atelier.atelier.entity.MessagingSystem;

import com.atelier.atelier.entity.UserPortalManagment.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CHATROOM_TYPE")
@DiscriminatorOptions(force = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Chatroom {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "chatrooms")
    private List<Chatter> chatters;

    @OneToMany(mappedBy = "chatroom")
    private List<Message> messages;

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

    public List<Chatter> getChatters() {
        return chatters;
    }

    public void setChatters(List<Chatter> chatters) {
        this.chatters = chatters;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addChatter(Chatter chatter){
        if (chatters == null){
            chatters = new ArrayList<Chatter>();
        }
        chatters.add(chatter);
    }

    public void addMessage(Message message){
        if (messages == null){
            messages = new ArrayList<>();
        }
        messages.add(message);
    }
}
