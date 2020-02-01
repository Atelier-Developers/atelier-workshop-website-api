package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.EditUserContext;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.repository.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/allUsers")
    public ResponseEntity<Object>  showAllUser() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> showRoles(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        List<Role> roles = user.getRoles();
        return roles;
    }

    //TODO EDIT USER
    @PutMapping("/user/{userId}")
    public ResponseEntity<Object> changeEmailAndName(@PathVariable long userId, @RequestBody EditUserContext editUserContext){

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();

        if (editUserContext.getName() != null ){
            user.setName(editUserContext.getName());
        }

        if (editUserContext.getEmail() != null ){
            user.setEmail(editUserContext.getEmail());
        }

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}