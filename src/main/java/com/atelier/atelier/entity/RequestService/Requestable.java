package com.atelier.atelier.entity.RequestService;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.List;

@MappedSuperclass
public abstract class Requestable {

    @OneToMany(mappedBy = "requestable")
    private List<Request> requests;
}
