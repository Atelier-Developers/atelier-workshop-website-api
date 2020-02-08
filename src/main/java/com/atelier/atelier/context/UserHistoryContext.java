package com.atelier.atelier.context;

import java.util.List;

public class UserHistoryContext {

    private List<WorkshopAndFormContext> attendedWorkshops;
    private List<WorkshopAndFormContext> assistedWorkshops;

    public List<WorkshopAndFormContext> getAttendedWorkshops() {
        return attendedWorkshops;
    }

    public void setAttendedWorkshops(List<WorkshopAndFormContext> attendedWorkshops) {
        this.attendedWorkshops = attendedWorkshops;
    }

    public List<WorkshopAndFormContext> getAssistedWorkshops() {
        return assistedWorkshops;
    }

    public void setAssistedWorkshops(List<WorkshopAndFormContext> assistedWorkshops) {
        this.assistedWorkshops = assistedWorkshops;
    }
}
