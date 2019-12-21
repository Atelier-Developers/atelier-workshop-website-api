package com.atelier.atelier.service.Workshop;

import com.atelier.atelier.entity.WorkshopManagment.Workshop;

import java.util.List;

public interface WorkshopService {

    public List<Workshop> findAll();

    public Workshop findById(int id);

    public void save(Workshop workshop);

    public void deleteById(int id);


}
