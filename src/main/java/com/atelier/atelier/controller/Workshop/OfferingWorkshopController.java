package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.OfferingWorkshopContext;
import com.atelier.atelier.entity.UserPortalManagment.ManagerWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopForm;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshopManagers")
public class OfferingWorkshopController {

    private WorkshopRepository workshopRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private UserRepository userRepository;

    public OfferingWorkshopController(WorkshopRepository workshopRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/offeringWorkshop")
    public ResponseEntity<Object> showAllOfferedWorkshop(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("ManagerWorkshopConnection");
        if (role == null) {
            return new ResponseEntity<>("User has no workshop manager role", HttpStatus.NO_CONTENT);
        }
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) role;
        return new ResponseEntity<>(managerWorkshopConnection.getOfferedWorkshops(), HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop")
    public ResponseEntity<Object> addOfferingWorkshop(@RequestBody OfferingWorkshopContext offeringWorkshopContext, Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        long wid = offeringWorkshopContext.getWorkshopId();
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(wid);
        if (!optionalWorkshop.isPresent()) {
            return new ResponseEntity<>("The workshop with the id provided does not exist. ", HttpStatus.NO_CONTENT);
        }
        Workshop workshop = optionalWorkshop.get();
        OfferedWorkshop offeredWorkshop = offeringWorkshopContext.getOfferedWorkshop();
        offeredWorkshop.setWorkshop(workshop);
        workshop.addOfferingWorkshop(offeredWorkshop); // BI DIRECTIONAL MAPPING
        offeredWorkshop.setWorkshopManager(managerWorkshopConnection);
        offeringWorkshopRepository.save(offeredWorkshop);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/offeringWorkshop/{id}/forms")
    public ResponseEntity<Object> showAllOfferedWorkshopForms(Authentication authentication, @PathVariable Long id) {
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                return new ResponseEntity<>(offeredWorkshop.getWorkshopForms(), HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

}
