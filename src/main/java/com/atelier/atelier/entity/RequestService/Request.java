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

    @Any(metaColumn = @Column(name = "REQUEST_DATA_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {

            })
    @JoinColumn(name="REQUEST_DATAs_ID")
    private List<RequestData> requestData;

    @Enumerated(EnumType.ORDINAL)
    private RequestState state;

    @ManyToOne
    @JoinColumn(name = "requestable_id")
    private Requestable requestable;


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

    public List<RequestData> getRequestData() {
        return requestData;
    }

    public void setRequestData(List<RequestData> requestData) {
        this.requestData = requestData;
    }

    public Requestable getRequestable() {
        return requestable;
    }

    public void setRequestable(Requestable requestable) {
        this.requestable = requestable;
    }
}
