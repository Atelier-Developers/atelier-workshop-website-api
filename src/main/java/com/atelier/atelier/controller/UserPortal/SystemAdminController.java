package com.atelier.atelier.controller.UserPortal;

import com.atelier.atelier.entity.UserPortalManagment.SystemAdmin;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.AttenderPaymentTab;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.Request.AttenderPaymentTabRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class SystemAdminController {

    private WorkshopRepository workshopRepository;
    private UserRepository userRepository;
    private AttenderPaymentTabRepository attenderPaymentTabRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;

    public SystemAdminController(OfferingWorkshopRepository offeringWorkshopRepository, AttenderPaymentTabRepository attenderPaymentTabRepository, WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.attenderPaymentTabRepository = attenderPaymentTabRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
    }

    // TODO for test remove for production
    @PostMapping("/makeAdmin")
    public ResponseEntity<Object> makeAdmin(Authentication authentication){
        User user = User.getUser(authentication, userRepository);
        if(user.getUsername().equals("admin")){
            SystemAdmin systemAdmin = new SystemAdmin();
            user.addRole(systemAdmin);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/workshops")
    public ResponseEntity<Object> getWorkshops(Authentication authentication){

        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if (systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        List<Workshop> workshops = workshopRepository.findAll();

        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

    @DeleteMapping("/offeringWorkshop/{id}")
    public ResponseEntity<Object> deleteOfferedWorkshop(@PathVariable long id, Authentication authentication){
        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if ( systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        offeringWorkshopRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/workshops")
    public ResponseEntity<Object> save(@RequestBody Workshop workshop, Authentication authentication){

        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if ( systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        workshop.setId(0);

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return new ResponseEntity<>(savedWorkshop, HttpStatus.CREATED);
    }


    @PutMapping("/workshops")
    public ResponseEntity<Object> update(@RequestBody Workshop workshop, Authentication authentication){

        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if ( systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshop.getId());

        if ( !optionalWorkshop.isPresent() ){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return new ResponseEntity<>(savedWorkshop, HttpStatus.OK);
    }

    @DeleteMapping("/workshops/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id, Authentication authentication){

        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if ( systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }


        Optional<Workshop> optionalWorkshop = workshopRepository.findById(id);

        if ( !optionalWorkshop.isPresent() ){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        workshopRepository.delete(optionalWorkshop.get());

        return new ResponseEntity<>("Item was deleted", HttpStatus.OK);

    }

    @PutMapping("/attendeePaymentTab/{id}")
    public ResponseEntity<Object> acceptPaymentTabState(@PathVariable long id, Authentication authentication){
        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);
        if ( systemAdmin == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<AttenderPaymentTab> optionalAttenderPaymentTab = attenderPaymentTabRepository.findById(id);
        if ( !optionalAttenderPaymentTab.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        AttenderPaymentTab attenderPaymentTab = optionalAttenderPaymentTab.get();
        attenderPaymentTab.setPaid(true);
        attenderPaymentTabRepository.save(attenderPaymentTab);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private SystemAdmin getSysAdminRoleFromAuthentication(Authentication authentication){

        User user = User.getUser(authentication, userRepository);
        SystemAdmin systemAdmin = (SystemAdmin) user.getRole("SystemAdmin");
        return systemAdmin;

    }
}
