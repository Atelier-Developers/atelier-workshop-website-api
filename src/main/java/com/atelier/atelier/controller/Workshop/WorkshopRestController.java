package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.controller.ExceptionHandling.NotFoundException;
import com.atelier.atelier.entity.UserPortalManagment.ManagerWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshop")
public class WorkshopRestController {

    private WorkshopRepository workshopRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;

    @Autowired
    public WorkshopRestController(OfferingWorkshopRepository offeringWorkshopRepository, WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
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

    @GetMapping("/offeringWorkshop")
    public ResponseEntity<Object> showAllOfferedWorkshop() {

        return new ResponseEntity<>(offeringWorkshopRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/offeringWorkshop/{id}")
    public ResponseEntity<Object> showAllOfferedWorkshopForms(@PathVariable Long id) {
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            return new ResponseEntity<>(offeredWorkshop, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


}
