package com.atelier.atelier.entity.WorkshopManagment;

import com.atelier.atelier.entity.RequestService.Requestable;

import java.util.Calendar;
import java.util.List;

public class OfferedWorkshop extends Requestable {
    private Calendar startTime;
    private Calendar endTime;
    private List<WorkshopForm> workshopForms;
    private List<WorkshopAttenderInfo> atenderInfos;
    private List<OfferedWorkshopRelationDetail> workshopRelationDetails;
}
