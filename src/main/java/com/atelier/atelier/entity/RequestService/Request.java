package com.atelier.atelier.entity.RequestService;


import com.atelier.atelier.entity.WorkshopManagment.AttenderRegisterForm;
import com.atelier.atelier.entity.WorkshopManagment.AttenderRequestPaymentTab;
import com.atelier.atelier.entity.WorkshopManagment.GraderRequestForm;
import com.sun.xml.bind.v2.TODO;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
    private List<RequestData> requestData;

    @Enumerated(EnumType.ORDINAL)
    private RequestState state;

    @ManyToOne
    @JoinColumn(name = "requestable_id")
    private Requestable requestable;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Requester requester;


}
