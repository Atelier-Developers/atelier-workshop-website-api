package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.Workshop;

import java.util.List;

public interface WorkshopDAO {
    List<Workshop> findAll();

    Workshop findById(long id);

    void save(Workshop workshop);

    void deleteById(long id);
}
