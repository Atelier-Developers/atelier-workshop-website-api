package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.EnrollCountContext;
import com.atelier.atelier.context.OfferedWorkshopManagerNameContext;
import com.atelier.atelier.controller.ExceptionHandling.NotFoundException;
import com.atelier.atelier.entity.UserPortalManagment.ManagerWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopManager;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import org.apache.coyote.Response;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshop")
public class WorkshopRestController {

    private WorkshopRepository workshopRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private UserRepository userRepository;

    @Autowired
    public WorkshopRestController(UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/workshops/offeredWorkshop/{offeredWorkshopId}")
    public ResponseEntity<Object> getOfferedWorkshopCount(@PathVariable long offeredWorkshopId){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeredWorkshopId);

        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        int attenderCount = offeredWorkshop.getAttenderInfos().size();

        EnrollCountContext enrollCountContext = new EnrollCountContext();

        enrollCountContext.setCount(attenderCount);

        return new ResponseEntity<>(enrollCountContext, HttpStatus.OK);


    }


    @GetMapping("/workshops/offeredWorkshops/att/{offeredWorkshopId}")
    public ResponseEntity<Object> getAttendeeInfosById(@PathVariable long offeredWorkshopId){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeredWorkshopId);

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getAttenderInfos(), HttpStatus.OK);

    }


    @GetMapping("/workshops")
    public ResponseEntity<Object> getAllWorkshops(){
        List<Workshop> workshops = workshopRepository.findAll();
        if (workshops.isEmpty()){
            return new ResponseEntity<>("No workshops were found", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

    @GetMapping("/workshops/{workshopId}")
    public ResponseEntity<Object> getWorkshopById(@PathVariable long workshopId){
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
    public ResponseEntity<Object> showOfferedWorkshopById(@PathVariable Long id) {
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            return new ResponseEntity<>(offeredWorkshop, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    @GetMapping("/offeringWorkshops")
    public ResponseEntity<Object> showOfferingWorkshopsForHomePage(){

        List<OfferedWorkshop> offeredWorkshops = offeringWorkshopRepository.findAll();
        List<User> users = userRepository.findAll();

        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();

        for ( OfferedWorkshop offeredWorkshop : offeredWorkshops){

            long workshopManagerId = offeredWorkshop.getWorkshopManager().getId();

            User workshopManagerUser = null;
            for ( User user: users){

                WorkshopManager workshopManagerRole = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                if (workshopManagerRole.getId() == workshopManagerId){
                    workshopManagerUser = user;
                    break;
                }

            }

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);
            offeredWorkshopManagerNameContext.setWorkshopManagerName(workshopManagerUser.getName());

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/{id}")
    public ResponseEntity<Object> showSingleOfferingWorkshopByIdForWorkshopPage(@PathVariable long id){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<User> users = userRepository.findAll();

        for ( User user : users ){

            WorkshopManager workshopManager = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

            if (workshopManager.getId() == id){

                OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();

                offeredWorkshopManagerNameContext.setWorkshopManagerName(user.getName());
                offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

                return new ResponseEntity<>(offeredWorkshopManagerNameContext, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
