package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.UserPortalManagment.File;
import com.atelier.atelier.entity.UserPortalManagment.User;

import javax.persistence.*;
import java.net.URL;
import java.util.List;


@Entity
public class PersonalFile {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private File file;

    @Column
    private URL urlLink;

    @Enumerated(EnumType.ORDINAL)
    private WorkshopFileType workshopFileType;

    @Enumerated(EnumType.ORDINAL)
    private PersonalFileType personalFileType;

    @ManyToOne
    @JoinColumn(name = "workshop_attender_info_id")
    private WorkshopAttenderInfo workshopAttenderInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public URL getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(URL urlLink) {
        this.urlLink = urlLink;
    }

    public WorkshopFileType getWorkshopFileType() {
        return workshopFileType;
    }

    public void setWorkshopFileType(WorkshopFileType workshopFileType) {
        this.workshopFileType = workshopFileType;
    }

    public PersonalFileType getPersonalFileType() {
        return personalFileType;
    }

    public void setPersonalFileType(PersonalFileType personalFileType) {
        this.personalFileType = personalFileType;
    }

    public WorkshopAttenderInfo getWorkshopAttenderInfo() {
        return workshopAttenderInfo;
    }

    public void setWorkshopAttenderInfo(WorkshopAttenderInfo workshopAttenderInfo) {
        this.workshopAttenderInfo = workshopAttenderInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
