package com.atelier.atelier.entity.RequestService;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REQUESTABLE_TYPE")

public abstract class Requestable {

    @Id
    @GeneratedValue()
    private long id;

    @OneToMany(mappedBy = "requestable")
    private List<Request> requests;

}
