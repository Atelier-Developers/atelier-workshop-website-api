package com.atelier.atelier.entity.RequestService;


import com.atelier.atelier.entity.WorkshopManagment.AttenderRegisterForm;
import com.atelier.atelier.entity.WorkshopManagment.AttenderRequestPaymentTab;
import com.atelier.atelier.entity.WorkshopManagment.GraderRequestForm;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.xml.bind.v2.TODO;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToAny(metaColumn = @Column(name = "request_data_types"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = GraderRequestForm.class, value = "GraderRequestForm"),
                    @MetaValue(targetEntity = AttenderRegisterForm.class, value = "AttenderRegisterForm"),
                    @MetaValue(targetEntity = AttenderRequestPaymentTab.class, value = "AttenderRequestPaymentTab")
            })
    @JoinTable(name = "request_request_data",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "request_data_id"))
    private List<RequestData> requestDatas;

    @Enumerated(EnumType.ORDINAL)
    private RequestState state;

    @ManyToOne
    @JoinColumn(name = "requestable_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Requestable requestable;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Requester requester;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<RequestData> getRequestData() {
        return requestDatas;
    }

    public void setRequestData(List<RequestData> requestData) {
        this.requestDatas = requestData;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public Requestable getRequestable() {
        return requestable;
    }

    public void setRequestable(Requestable requestable) {
        this.requestable = requestable;
    }

    public Requester getRequester() {
        return requester;
    }

    public void setRequester(Requester requester) {
        this.requester = requester;
    }

    public void addRequestData(RequestData requestData){
        if(requestDatas == null){
            requestDatas = new ArrayList<>();
        }
        requestDatas.add(requestData);
    }
}
