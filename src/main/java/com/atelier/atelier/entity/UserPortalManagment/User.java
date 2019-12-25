package com.atelier.atelier.entity.UserPortalManagment;

import com.atelier.atelier.repository.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){

        if ( roles == null ){
            roles = new ArrayList<>();
        }

        roles.add(role);
    }

    public Role getRole(String classType){
        for(Role role : roles){
            if(role.getClass().getSimpleName().equals(classType)){
                return role;
            }
        }
        return null;
    }

    public static User getUser(Authentication authentication, UserRepository userRepository){
        String userName = authentication.getName();
        User user =  userRepository.findByUsername(userName);
        return user;
    }
}
