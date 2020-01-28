package com.atelier.atelier.entity.WorkshopManagment;


import com.atelier.atelier.entity.UserPortalManagment.File;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class WorkshopFile {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private File file;

    @ElementCollection(targetClass = WorkshopFileReceiver.class)
    @JoinTable(name = "receivers", joinColumns = @JoinColumn(name = "workshop_file_id"))
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private List<WorkshopFileReceiver> receivers;

    @ManyToOne
    @JoinColumn(name = "offered_workshop_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OfferedWorkshop offeredWorkshop;

    public WorkshopFile() {
    }

    public WorkshopFile(String title, String description, File file) {
        this.title = title;
        this.description = description;
        this.file = file;
    }

    public OfferedWorkshop getOfferedWorkshop() {
        return offeredWorkshop;
    }

    public void setOfferedWorkshop(OfferedWorkshop offeredWorkshop) {
        this.offeredWorkshop = offeredWorkshop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<WorkshopFileReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<WorkshopFileReceiver> receivers) {
        this.receivers = receivers;
    }
}
