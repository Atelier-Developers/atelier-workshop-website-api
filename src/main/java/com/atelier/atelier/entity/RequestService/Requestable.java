package com.atelier.atelier.entity.RequestService;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTABLE_TYPE")

public abstract class Requestable {

    @Id
    @GeneratedValue
    protected long id;

    @OneToMany(mappedBy = "requestable")
    private List<Request> requests;

    public Requestable(){

    }

    public Requestable(List<Request> requests) {
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
