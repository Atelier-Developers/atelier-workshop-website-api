package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.controller.ExceptionHandling.NotFoundException;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshop")
public class WorkshopRestController {

    private WorkshopRepository workshopRepository;

    @Autowired
    public WorkshopRestController(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @GetMapping("/workshops")
    public ResponseEntity<Object> getWorkshops(){
        List<Workshop> workshops = workshopRepository.findAll();
        if (workshops.isEmpty()){
            return new ResponseEntity<>("No workshops were found", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

    @GetMapping("/workshops/{workshopId}")
    public ResponseEntity<Object> getById(@PathVariable long workshopId){
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshopId);

        if ( !optionalWorkshop.isPresent() ){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop workshop = optionalWorkshop.get();
        return new ResponseEntity<>(workshop, HttpStatus.OK);
    }

    @PostMapping("/workshops")
    public ResponseEntity<Object> save(@RequestBody Workshop workshop){

        workshop.setId(0);

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return new ResponseEntity<>(workshop, HttpStatus.CREATED);
    }


    @PutMapping("/workshops")
    public ResponseEntity<Object> update(@RequestBody Workshop workshop){
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshop.getId());

        if ( !optionalWorkshop.isPresent() ){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return new ResponseEntity<>(savedWorkshop, HttpStatus.OK);
    }

    @DeleteMapping("/workshops/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id){

        Optional<Workshop> optionalWorkshop = workshopRepository.findById(id);

        if ( !optionalWorkshop.isPresent() ){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        workshopRepository.deleteById(id);

        return new ResponseEntity<>("Item was deleted", HttpStatus.OK);

    }
}
