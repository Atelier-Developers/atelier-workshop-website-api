package com.atelier.atelier.entity.RequestService;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTABLE_TYPE")

public abstract class Requestable {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "requestable")
    private List<Request> requests;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
