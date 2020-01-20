package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.EnrollCountContext;
import com.atelier.atelier.context.OfferedWorkshopManagerNameContext;
import com.atelier.atelier.context.OfferedWorkshopUserListsContext;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.Collections;
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


    @GetMapping("/workshops/offeredWorkshop/{offeredWorkshopId}/count")
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

    @GetMapping("/workshops/{workshopId}/offeringWorkshop")
    public ResponseEntity<Object> getOfferingWorkshopsOfAWorkshop(@PathVariable long workshopId){

        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshopId);

        if (!optionalWorkshop.isPresent()){
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop workshop = optionalWorkshop.get();

        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();
        List<User> users = userRepository.findAll();

        for ( OfferedWorkshop offeredWorkshop : workshop.getOfferedWorkshops() ){

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();

            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            WorkshopManager workshopManager = offeredWorkshop.getWorkshopManager();

            for ( User user : users ){

                WorkshopManager workshopManager1 = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                if (workshopManager1.getId() == workshopManager.getId()){

                    offeredWorkshopManagerNameContext.setWorkshopManagerName(user.getName());
                    break;
                }

            }

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
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

        OfferedWorkshopUserListsContext offeredWorkshopUserListsContext = new OfferedWorkshopUserListsContext();

        offeredWorkshopUserListsContext.setOfferedWorkshop(offeredWorkshop);

        for ( User user : users ){

            WorkshopManager workshopManager = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

            if (workshopManager.getId() == offeredWorkshop.getWorkshopManager().getId()){

                offeredWorkshopUserListsContext.setWorkshopManagerUser(user);
                break;
            }
        }

        List<User> attendeeUsers = new ArrayList<User>();
        for (WorkshopAttenderInfo workshopAttenderInfo : offeredWorkshop.getAttenderInfos()){

            WorkshopAttender workshopAttender = workshopAttenderInfo.getWorkshopAttender();

            for ( User user : users ){

                Attender attender = (Attender) user.getRole("Attender");

                WorkshopAttender workshopAttender1 = attender.getAttenderWorkshopConnection();

                if ( workshopAttender1.getId() == workshopAttender.getId() ){

                    attendeeUsers.add(user);

                }
            }
        }

        List<User> graderUsers = new ArrayList<User>();
        for (WorkshopGraderInfo workshopGraderInfo : offeredWorkshop.getWorkshopGraderInfos()){

            WorkshopGrader workshopGrader = workshopGraderInfo.getWorkshopGrader();

            for (User user : users){

                Grader grader = (Grader) user.getRole("Grader");

                WorkshopGrader workshopGrader1 = grader.getGraderWorkshopConnection();

                if (workshopGrader1.getId() == workshopGrader.getId()){

                    graderUsers.add(user);
                }
            }
        }

        List<String> preReqs = new ArrayList<String>();
        for ( OfferedWorkshopRelationDetail offeredWorkshopRelationDetail : offeredWorkshop.getWorkshopRelationDetails()){

            Optional<Workshop> optionalWorkshop = workshopRepository.findById(offeredWorkshopRelationDetail.getWorkshop().getId());

            Workshop workshop = optionalWorkshop.get();

            preReqs.add(workshop.getName());


        }


        offeredWorkshopUserListsContext.setAttendeeUsers(attendeeUsers);
        offeredWorkshopUserListsContext.setGraderUsers(graderUsers);
        offeredWorkshopUserListsContext.setPreRequisites(preReqs);


        return new ResponseEntity<>(offeredWorkshopUserListsContext, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/popular")
    public ResponseEntity<Object> showFirstFivePopularWorkshops(){

        List<OfferedWorkshop> offeredWorkshops = offeringWorkshopRepository.findAll();

        if (offeredWorkshops.size() > 5 ){
            List<OfferedWorkshop> result = offeredWorkshops.subList(0, 5);
            Collections.sort(result, Collections.reverseOrder());

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        else {

            Collections.sort(offeredWorkshops, Collections.reverseOrder());


            return new ResponseEntity<>(offeredWorkshops, HttpStatus.OK);
        }


    }

}
