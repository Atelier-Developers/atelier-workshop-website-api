package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.PaymentService.Payment;
import com.atelier.atelier.entity.RequestService.RequestData;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class AttenderRequestPaymentTab implements RequestData {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "attenderRequestPaymentTab", cascade = CascadeType.ALL)
    private List<AttenderPaymentTab> attenderPaymentTabList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AttenderPaymentTab> getAttenderPaymentTabList() {
        return attenderPaymentTabList;
    }

    public void setAttenderPaymentTabList(List<AttenderPaymentTab> attenderPaymentTabList) {
        this.attenderPaymentTabList = attenderPaymentTabList;
    }

    public void addPayment(AttenderPaymentTab attenderPaymentTab){
        if(attenderPaymentTabList == null){
            attenderPaymentTabList = new ArrayList<>();
        }
        attenderPaymentTabList.add(attenderPaymentTab);
    }
}
