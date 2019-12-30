package com.atelier.atelier.controller.UserPortal;

import com.atelier.atelier.entity.UserPortalManagment.SystemAdmin;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.AttenderPaymentTab;
import com.atelier.atelier.entity.WorkshopManagment.AttenderRequestPaymentTab;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.Request.AttenderPaymentTabRepository;
import com.atelier.atelier.repository.Request.AttenderRequestPaymentTabRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class SystemAdminController {

    private WorkshopRepository workshopRepository;
    private UserRepository userRepository;
    private AttenderPaymentTabRepository attenderPaymentTabRepository;

    public SystemAdminController(AttenderPaymentTabRepository attenderPaymentTabRepository, WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.attenderPaymentTabRepository = attenderPaymentTabRepository;
    }


    @PostMapping("/workshops")
    public ResponseEntity<Object> save(@RequestBody Workshop workshop, Authentication authentication){

        SystemAdmin systemAdmin = getSysAdminRoleFromAuthentication(authentication);

        if ( systemAdmin == null ){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        //TODO CHECK THIS.
        workshop.setId(0);

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return new ResponseEntity<>(workshop, HttpStatus.CREATED);
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

        workshopRepository.deleteById(id);

        return new ResponseEntity<>("Item was deleted", HttpStatus.OK);

    }

    @PutMapping("/attendeePaymentTab/{id}/")
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
