package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.entity.FormService.ChoiceAnswer;
import com.atelier.atelier.entity.FormService.FileAnswer;
import com.atelier.atelier.entity.FormService.TextAnswer;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToAny(metaColumn = @Column(name = "role_type"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = SystemAdmin.class, value = "SystemAdmin"),
                    @MetaValue(targetEntity = Grader.class, value = "Grader"),
                    @MetaValue(targetEntity = ManagerWorkshopConnection.class, value = "ManagerWorkshopConnection"),
                    @MetaValue(targetEntity = Attender.class, value = "Attender")
            })
    @Cascade( { org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
