package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.entity.UserPortalManagment.Grader;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopGrader;
import com.atelier.atelier.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userDetails")
public class PublicUserController {


    private UserRepository userRepository;


    public PublicUserController( UserRepository userRepository) {
        this.userRepository = userRepository;
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


    /// GET WORKSHOPS (PASSED, SOON TO BE HELD, PENDING)

//    @GetMapping("/grader/{graderId}")
//

}
