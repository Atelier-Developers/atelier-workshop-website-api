package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.RequestService.RequestData;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class AttenderRequestPaymentTab implements RequestData {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "attenderRequestPaymentTab")
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
}
