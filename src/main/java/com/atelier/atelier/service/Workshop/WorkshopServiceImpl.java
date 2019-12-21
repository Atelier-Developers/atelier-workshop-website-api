package com.atelier.atelier.service.Workshop;

import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.workshop.WorkshopDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    private WorkshopDAO workshopDAO;

    @Autowired
    public WorkshopServiceImpl(WorkshopDAO workshopDAO) {
        this.workshopDAO = workshopDAO;
    }

    @Override
    @Transactional
    public List<Workshop> findAll() {
        return workshopDAO.findAll();
    }

    @Override
    @Transactional
    public Workshop findById(int id) {
        return workshopDAO.findById(id);
    }

    @Override
    @Transactional
    public void save(Workshop workshop) {
        workshopDAO.save(workshop);
    }

    @Override
    public void deleteById(int id) {
        workshopDAO.deleteById(id);
    }
}
