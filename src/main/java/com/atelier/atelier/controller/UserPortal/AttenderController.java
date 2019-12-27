package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.OfferingWorkshopContext;
import com.atelier.atelier.entity.UserPortalManagment.Attender;
import com.atelier.atelier.entity.UserPortalManagment.AttenderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttenderInfo;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.hibernate.jdbc.Work;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO SYS ADMIN HAS TO HAVE PATHS FOR CREATING ROLES FOR USERS
// TODO FOR REGISTRATION RETRIEVE GRADERS INFO LIST AND ATTENDER'S INFO LIST FOR A OFFERING WORKSHOP AND
// TODO HAVE THE WORKSHOP MANGER FORM GROUPS BASED ON THESE LISTS
// TODO CHECK ATTENDER'S PERMISSION'S FOR ENROLLMENT (SEE IF HE HAS PASSED THE PRE REQUISTIES, IF HE HAS ENROLLED IN THE SAME WORKSHOP)

@RestController
@RequestMapping("/attendees")
public class AttenderController {

    private AttenderRepository attenderRepository;
    private UserRepository userRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;

    public AttenderController(AttenderRepository attenderRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository) {
        this.attenderRepository = attenderRepository;
        this.userRepository = userRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
    }

    @GetMapping("/attendee")
    public ResponseEntity<Object> getAttendeeRole(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The attendee role was not found for the user", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        return new ResponseEntity<>(attenderRole, HttpStatus.OK);
    }

    @GetMapping("/attendee/workshops")
    public ResponseEntity<Object> getAttenderWorkshops(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The user has no attendee role.", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        AttenderWorkshopConnection attenderWorkshopConnection = attenderRole.getAttenderWorkshopConnection();
        if ( attenderWorkshopConnection == null) {
            return new ResponseEntity<>("The user has no attendee workshop connection.", HttpStatus.NO_CONTENT);
        }
        List<WorkshopAttenderInfo> workshopAttenderInfos = attenderWorkshopConnection.getWorkshopAttenderInfos();
        if (workshopAttenderInfos.isEmpty()) {
            return new ResponseEntity<>("The user has no attendee workshop attendee info", HttpStatus.NO_CONTENT);
        }
        List<OfferedWorkshop> workshops = new ArrayList<>();
        for (WorkshopAttenderInfo workshopAttenderInfo : workshopAttenderInfos) {
            workshops.add(workshopAttenderInfo.getOfferedWorkshop());
        }
        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }


    @PostMapping("/attendee/{offeringWorkshopId}")
    public ResponseEntity<Object> enrollAttendeeAtOfferingWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The user does not have an attendee role.", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        AttenderWorkshopConnection attenderWorkshopConnection = attenderRole.getAttenderWorkshopConnection();

        WorkshopAttenderInfo workshopAttenderInfo = new WorkshopAttenderInfo();
        workshopAttenderInfo.setOfferedWorkshop(offeredWorkshop);
        workshopAttenderInfo.setWorkshopAttender(attenderWorkshopConnection);
        attenderWorkshopConnection.addWorkshopAttenderInfo(workshopAttenderInfo);
        offeredWorkshop.addWorkshopAttenderInfo(workshopAttenderInfo);
        workshopAttenderInfoRepository.save(workshopAttenderInfo);
        return new ResponseEntity<>(workshopAttenderInfo, HttpStatus.OK);


    }


}
