package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userDetails")
public class PublicUserController {


    private UserRepository userRepository;


    public PublicUserController( UserRepository userRepository) {
        this.userRepository = userRepository;
    }


//    @GetMapping("/workshopGrader/{workshopGraderId}")
//    public ResponseEntity<Object> findUserByWorkshopGraderId(@PathVariable long workshopGraderId){
//
//        List<Use>
//    }
}
