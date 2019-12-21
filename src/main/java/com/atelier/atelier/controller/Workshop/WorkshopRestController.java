package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.controller.ExceptionHandling.NotFoundException;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.service.Workshop.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workshop")
public class WorkshopRestController {
    private WorkshopService workshopService;

    @Autowired
    public WorkshopRestController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping("/workshops")
    public List<Workshop> getWorkshops(){
        return workshopService.findAll();
    }

    @GetMapping("/workshops/{workshopId}")
    public Workshop getById(@PathVariable long workshopId){
        Workshop workshop = workshopService.findById(workshopId);

        if ( workshop == null ){
            throw new NotFoundException("Workshop id not found - " + workshopId);
        }

        return workshop;
    }

    @PostMapping("/workshops")
    public Workshop save(@RequestBody Workshop workshop){

        workshop.setId(0);

        workshopService.save(workshop);

        return workshop;
    }


    @PutMapping("/workshops")
    public Workshop update(@RequestBody Workshop workshop){
        Workshop workshop1 = workshopService.findById(workshop.getId());

        if (workshop1 == null){
            throw new NotFoundException("Workshop id not found - " + workshop.getId());
        }

        workshopService.save(workshop);

        return workshop;
    }

    @DeleteMapping("/workshops/{id}")
    public void delete(@PathVariable long id){

        Workshop workshop = workshopService.findById(id);

        if (workshop == null){
            throw new NotFoundException("Workshop id not found - " + id);
        }

        workshopService.deleteById(id);

    }
}
