package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.FormService.FileAnswer;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.Requestable;
import com.atelier.atelier.entity.UserPortalManagment.File;
import com.fasterxml.jackson.annotation.*;
import org.joda.time.Interval;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.*;


@Entity
@DiscriminatorValue(value = "OfferedWorkshop")
public class OfferedWorkshop extends Requestable implements Comparable<OfferedWorkshop> {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column( nullable = false)
    private String name;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String description;

    @OneToOne
    private File profileImage;


    @ManyToOne
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @ManyToOne
    @JoinColumn(name = "workshop_manager_id")
    private WorkshopManager workshopManager;

    @OneToMany(mappedBy = "offeredWorkshop")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopForm> workshopForms;

    @OneToMany(mappedBy = "offeredWorkshop")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopAttenderInfo> attenderInfos;

    @OneToMany(mappedBy = "offeredWorkshop")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<WorkshopGraderInfo> workshopGraderInfos;

    @OneToMany(mappedBy = "offeredWorkshop", cascade = CascadeType.ALL)
    private List<OfferedWorkshopRelationDetail> workshopRelationDetails;

    @OneToOne(mappedBy = "offeredWorkshop", cascade = CascadeType.ALL)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GraderEvaluationForm graderEvaluationForm;


    @OneToOne
    @JoinColumn(name = "grader_request_form_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GraderRequestForm graderRequestForm;

    @OneToOne
    @JoinColumn(name = "attender_register_form_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AttenderRegisterForm attenderRegisterForm;

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public List<WorkshopGraderInfo> getWorkshopGraderInfos() {
        return workshopGraderInfos;
    }

    public void setWorkshopGraderInfos(List<WorkshopGraderInfo> workshopGraderInfos) {
        this.workshopGraderInfos = workshopGraderInfos;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }


    public WorkshopManager getWorkshopManager() {
        return workshopManager;
    }

    public void setWorkshopManager(WorkshopManager workshopManager) {
        this.workshopManager = workshopManager;
    }


    public List<WorkshopForm> getWorkshopForms() {
        return workshopForms;
    }

    public void setWorkshopForms(List<WorkshopForm> workshopForms) {
        this.workshopForms = workshopForms;
    }

    public GraderEvaluationForm getGraderEvaluationForm() {
        return graderEvaluationForm;
    }

    public void setGraderEvaluationForm(GraderEvaluationForm graderEvaluationForm) {
        this.graderEvaluationForm = graderEvaluationForm;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public File getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }

    public List<WorkshopAttenderInfo> getAttenderInfos() {
        return attenderInfos;
    }

    public void setAttenderInfos(List<WorkshopAttenderInfo> attenderInfos) {
        this.attenderInfos = attenderInfos;
    }


    public List<OfferedWorkshopRelationDetail> getWorkshopRelationDetails() {
        return workshopRelationDetails;
    }

    public void setWorkshopRelationDetails(List<OfferedWorkshopRelationDetail> workshopRelationDetails) {
        this.workshopRelationDetails = workshopRelationDetails;
    }

    public void addWorkshopAttenderInfo(WorkshopAttenderInfo workshopAttenderInfo) {
        if (attenderInfos == null) {
            attenderInfos = new ArrayList<WorkshopAttenderInfo>();
        }

        attenderInfos.add(workshopAttenderInfo);
    }

    public void addWorkshopGraderrInfo(WorkshopGraderInfo workshopGraderInfo) {
        if (workshopGraderInfos == null) {
            workshopGraderInfos = new ArrayList<>();
        }

        workshopGraderInfos.add(workshopGraderInfo);
    }

    public void addWorkshopForm(WorkshopForm workshopForm) {
        if (workshopForms == null) {
            workshopForms = new ArrayList<>();
        }

        workshopForms.add(workshopForm);
    }

    public GraderRequestForm getGraderRequestForm() {
        return graderRequestForm;
    }

    public void setGraderRequestForm(GraderRequestForm graderRequestForm) {
        this.graderRequestForm = graderRequestForm;
    }

    public AttenderRegisterForm getAttenderRegisterForm() {
        return attenderRegisterForm;
    }

    public void setAttenderRegisterForm(AttenderRegisterForm attenderRegisterForm) {
        this.attenderRegisterForm = attenderRegisterForm;
    }

    public boolean hasGraderRequested(WorkshopGrader workshopGrader) {
        for (WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos) {
            if (workshopGraderInfo.getWorkshopGrader().getId() == workshopGrader.getId()) {
                return false;
            }
        }
        return true;
    }


    public boolean hasAtendeeRequested(WorkshopAttender workshopAttender) {
        for (WorkshopAttenderInfo workshopAttenderInfo : attenderInfos) {
            if (workshopAttenderInfo.getWorkshopAttender().getId() == workshopAttender.getId()) {
                return false;
            }
        }
        return true;
    }

    public static boolean doTwoOfferedWorkshopTimeIntervalsOverlap(OfferedWorkshop offeredWorkshop1, OfferedWorkshop offeredWorkshop2) {
        Calendar start1 = offeredWorkshop1.startTime;
        Calendar end1 = offeredWorkshop1.endTime;
        Calendar start2 = offeredWorkshop2.startTime;
        Calendar end2 = offeredWorkshop2.endTime;

        if (start1.before(start2) && end1.after(end2)) {
            return true;
        }

        if (start1.before(start2) && (end1.before(end2) && end1.after(start2))) {
            return true;
        }

        if (end1.after(end2) && (start1.after(start2) && start1.before(end2))) {
            return true;
        }

        if ((start1.after(start2) && start1.before(end2)) && (end1.before(end2) && end1.after(start2))) {
            return true;
        } else {
            return false;
        }
    }

    public void addOfferingWorkshopRelations(OfferedWorkshopRelationDetail offeredWorkshopRelationDetail) {
        if (workshopRelationDetails == null) {
            workshopRelationDetails = new ArrayList<>();
        }
        workshopRelationDetails.add(offeredWorkshopRelationDetail);
    }

    public Set<WorkshopGroup> workshopGroupSet() {
        Set<WorkshopGroup> workshopGroups = new HashSet<>();
        for (WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos) {
            workshopGroups.add(workshopGraderInfo.getWorkshopGroup());
        }
        for (WorkshopAttenderInfo workshopAttenderInfo : attenderInfos){
            workshopGroups.add(workshopAttenderInfo.getWorkshopGroup());
        }
        return workshopGroups;
    }


    @Override
    public int compareTo(OfferedWorkshop offWorkshop) {
        return (Integer.compare(this.getAttenderInfos().size(), offWorkshop.getAttenderInfos().size()));
    }
}
