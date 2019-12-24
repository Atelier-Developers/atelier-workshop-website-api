package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.GraderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.Role;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttenderInfo;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopGraderInfo;
import com.atelier.atelier.repository.role.GraderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopGraderInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// TODO ADD A GRADER WOKRSHOP CONNECTION WHEN THE ROLE IS BEING CREATED IN THE USER CONTROLLER (FOR TESTING)
// TODO WHEN ADDING AN OFFERING WORKSHOP TO THE GRADER, CHECK IF THE GRADER IS NOT AN ATTENDER IN THE SAME OFFERING WORKSHOP,
// TODO ALSO THEY AREN'T A GRADER IN THE SAME OFFERING WORKSHOP

@RestController
@RequestMapping("/graders")
public class GraderController {

    private UserRepository userRepository;
    private GraderRepository graderRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private WorkshopGraderInfoRepository workshopGraderInfoRepository;

    public GraderController(UserRepository userRepository, GraderRepository graderRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopGraderInfoRepository workshopGraderInfoRepository) {
        this.userRepository = userRepository;
        this.graderRepository = graderRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
    }


    @GetMapping("/grader")
    public ResponseEntity<Object> getGraderRole(Authentication authentication){
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Grader");
        if ( role == null ){
            return new ResponseEntity<>("The user has no grader role.", HttpStatus.NO_CONTENT);
        }
        Grader graderRole = (Grader) role;
        return new ResponseEntity<>(graderRole, HttpStatus.OK);
    }

    @GetMapping("/grader/workshops")
    public ResponseEntity<Object> getGraderWorkshops(Authentication authentication){
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Grader");
        if ( role == null ){
            return new ResponseEntity<>("This user has no grader role.", HttpStatus.NO_CONTENT );
        }
        Grader graderRole = (Grader) role;
        GraderWorkshopConnection graderWorkshopConnection = graderRole.getGraderWorkshopConnection();
        if ( graderWorkshopConnection == null ){
            return new ResponseEntity<>("This user has not grader workshop connection.", HttpStatus.NO_CONTENT);
        }
        List<WorkshopGraderInfo> workshopGraderInfos = graderWorkshopConnection.getWorkshopGraderInfos();
        if ( workshopGraderInfos.isEmpty() ){
            return new ResponseEntity<>("The user has no workshop grader info.", HttpStatus.NO_CONTENT);
        }
        List<OfferedWorkshop> workshops = new ArrayList<>();
        for (WorkshopGraderInfo workshopGraderInfo : workshopGraderInfos){
            workshops.add(workshopGraderInfo.getOfferedWorkshop());
        }

        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

    @PostMapping("/grader/{offeringWorkshopId}")
    public ResponseEntity<Object> enrollGraderAtOfferingWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication){
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Grader");
        if ( role == null ){
            return new ResponseEntity<>("This user has no grader role.", HttpStatus.NO_CONTENT );
        }
        Grader graderRole = (Grader) role;
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        GraderWorkshopConnection graderWorkshopConnection = graderRole.getGraderWorkshopConnection();
        WorkshopGraderInfo workshopGraderInfo = new WorkshopGraderInfo();
        workshopGraderInfo.setWorkshopGrader(graderWorkshopConnection);
        workshopGraderInfo.setOfferedWorkshop(offeredWorkshop);
        offeredWorkshop.addWorkshopGraderrInfo(workshopGraderInfo);
        graderWorkshopConnection.addGraderInfo(workshopGraderInfo);
        workshopGraderInfoRepository.save(workshopGraderInfo);
        return new ResponseEntity<>(workshopGraderInfo, HttpStatus.OK);
    }
}
