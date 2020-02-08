package com.atelier.atelier.context;

import com.atelier.atelier.entity.FormService.Question;

import java.util.List;

public class WorkshopAndFormContext {

    private OfferedWorkshopManagerNameContext workshop;
    private List<FormQAContext> form;

    public OfferedWorkshopManagerNameContext getWorkshop() {
        return workshop;
    }

    public void setWorkshop(OfferedWorkshopManagerNameContext workshop) {
        this.workshop = workshop;
    }

    public List<FormQAContext> getForm() {
        return form;
    }

    public void setForm(List<FormQAContext> form) {
        this.form = form;
    }
}
