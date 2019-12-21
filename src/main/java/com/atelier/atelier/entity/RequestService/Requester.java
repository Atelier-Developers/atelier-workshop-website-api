package com.atelier.atelier.entity.RequestService;

import java.util.List;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTER_TYPE")

public abstract class Requester {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "requester")
    protected List<Request> requests;
}
