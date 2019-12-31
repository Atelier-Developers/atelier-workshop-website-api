package com.atelier.atelier.entity.RequestService;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DiscriminatorOptions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTER_TYPE")
@DiscriminatorOptions(force = true)
public abstract class Requester {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "requester")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Request> requests;

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

    public void addRequest(Request request){
        if ( requests == null ){
            requests = new ArrayList<>();
        }

        requests.add(request);
    }
}
