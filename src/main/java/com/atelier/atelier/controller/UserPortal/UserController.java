package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import com.atelier.atelier.repository.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody User user) {
//        User foundUser = userRepository.findByUsername(user.getUsername());
//        if ( foundUser != null ){
//            return new ResponseEntity<>("A user with the username already exists", HttpStatus.BAD_REQUEST);
//        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Attender attender = new Attender();
        AttenderWorkshopConnection attenderWorkshopConnection = new AttenderWorkshopConnection();
        attender.setAttenderWorkshopConnection(attenderWorkshopConnection);
        attenderWorkshopConnection.setAttender(attender);

        user.addRole(attender);

        Grader grader = new Grader();
        GraderWorkshopConnection graderWorkshopConnection = new GraderWorkshopConnection();
        graderWorkshopConnection.setGrader(grader);
        grader.setGraderWorkshopConnection(graderWorkshopConnection);
        user.addRole(grader);

        user.addRole(new ManagerWorkshopConnection()); //TODO REMEMBER TO DELETE THESE AND ADD A PATH FOR ADDING SUCH ROLES TO A USER
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            return new ResponseEntity<>("This username has been used before",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Object>  showUser(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> showRoles(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        List<Role> roles = user.getRoles();
        return roles;
    }

}