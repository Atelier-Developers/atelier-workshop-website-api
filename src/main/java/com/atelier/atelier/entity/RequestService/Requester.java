package com.atelier.atelier.entity.RequestService;

import java.util.List;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTER_TYPE")

public abstract class Requester {
    @Id
    @GeneratedValue
    protected long id;

    @OneToMany(mappedBy = "requester")
    protected List<Request> requests;

    public Requester(){

    }

    public Requester(List<Request> requests) {
        this.requests = requests;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
