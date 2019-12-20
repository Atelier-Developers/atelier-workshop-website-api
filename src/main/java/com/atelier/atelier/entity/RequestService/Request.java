package com.atelier.atelier.entity.RequestService;


import com.sun.xml.bind.v2.TODO;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Any(metaColumn = @Column(name = "USER_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {

            })
    @JoinColumn(name="USER_ID")
    private List<RequestData> requestData;

    @Enumerated(EnumType.ORDINAL)
    private RequestState state;

    public Request() {
    }

    public Request( RequestState state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }


}
