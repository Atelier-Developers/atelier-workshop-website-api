package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.UserHitsoryContext;
import com.atelier.atelier.entity.UserPortalManagment.Attender;
import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.GraderWorkshopConnection;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userDetails")
public class PublicUserController {


    private UserRepository userRepository;
    private AttenderRepository attenderRepository;


    public PublicUserController( UserRepository userRepository, AttenderRepository attenderRepository) {
        this.userRepository = userRepository;
        this.attenderRepository = attenderRepository;
    }

    // GET USER THROUGH IDs APIs
    @GetMapping("/workshopGrader/{workshopGraderId}")
    public ResponseEntity<Object> findUserByWorkshopGraderId(@PathVariable long workshopGraderId){

        List<User> users = userRepository.findAll();

        for ( User user : users ){

            Grader grader = (Grader) user.getRole("Grader");

            WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

            if ( workshopGrader.getId() == workshopGraderId ){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /////////////////////////////// END OF USER FETCHING APIs



    //Returns graded, attended, managed workshops of a single user
    @GetMapping("/history/{id}")
    public ResponseEntity<Object> showUserHistory(@PathVariable long id){

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserHitsoryContext userHitsoryContext = new UserHitsoryContext();


        User user = optionalUser.get();

        Attender attender = (Attender) user.getRole("Attender");

        WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();

        List<OfferedWorkshop> attendedWorkshops = new ArrayList<>();

        for (WorkshopAttenderInfo workshopAttenderInfo : workshopAttender.getWorkshopAttenderInfos()){

            attendedWorkshops.add(workshopAttenderInfo.getOfferedWorkshop());

        }

        userHitsoryContext.setAttendedOfferedWorkshops(attendedWorkshops);

        Grader grader = (Grader) user.getRole("Grader");

        WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

        List<OfferedWorkshop> gradedWorkshops = new ArrayList<>();

        for (WorkshopGraderInfo workshopGraderInfo : workshopGrader.getWorkshopGraderInfos()){

            gradedWorkshops.add(workshopGraderInfo.getOfferedWorkshop());

        }

        userHitsoryContext.setGradedOfferedWorkshops(gradedWorkshops);


        WorkshopManager workshopManager = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

        userHitsoryContext.setManagedOfferedWorkshops(workshopManager.getOfferedWorkshops());

        return new ResponseEntity<>(userHitsoryContext, HttpStatus.OK);


    }

    ///TODO GET WORKSHOPS (PASSED, SOON TO BE HELD, PENDING)

//    @GetMapping("/attendee/{attendeeId}")
//    public ResponseEntity<Object> getGradersOfferedWorkshopsForGraderPage(@PathVariable long id){
//
//        Optional<Attender> optionalAttender = attenderRepository.findById(id);
//
//        if ( !optionalAttender.isPresent() ){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        Attender attender = optionalAttender.get();
//
//        List<OfferedWorkshop> passedWorkshops;
//        List<OfferedWorkshop> soonWorkshops;
//        List<OfferedWorkshop> currentWorkshops;
//        for (WorkshopAttenderInfo workshopAttenderInfo : attender.getAttenderWorkshopConnection().getWorkshopAttenderInfos()){
//
//            if ( workshopAttenderInfo.getAttendeeStatusType().equals(AttendeeStatusType.Passed)){
//
//            }
//        }
//
//    }


}
