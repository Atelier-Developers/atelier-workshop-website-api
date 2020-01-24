package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.OfferedWorkshopManagerNameContext;
import com.atelier.atelier.context.UserHitsoryContext;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.FileRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userDetails")
public class PublicUserController {


    private UserRepository userRepository;
    private AttenderRepository attenderRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private FileRepository fileRepository;


    public PublicUserController(FileRepository fileRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, AttenderRepository attenderRepository) {
        this.userRepository = userRepository;
        this.attenderRepository = attenderRepository;
        this.fileRepository = fileRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
    }

    // GET USER THROUGH IDs APIs
    @GetMapping("/workshopGrader/{workshopGraderId}")
    public ResponseEntity<Object> findUserByWorkshopGraderId(@PathVariable long workshopGraderId) {

        List<User> users = userRepository.findAll();

        for (User user : users) {

            Grader grader = (Grader) user.getRole("Grader");

            WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

            if (workshopGrader.getId() == workshopGraderId) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /////////////////////////////// END OF USER FETCHING APIs



    //Returns graded, attended, managed workshops of a single user in an OfferedWorkshopManageName
    @GetMapping("/history/{id}")
    public ResponseEntity<Object> showUserHistory(@PathVariable long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserHitsoryContext userHitsoryContext = new UserHitsoryContext();


        User user = optionalUser.get();

        Attender attender = (Attender) user.getRole("Attender");

        WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();

        List<OfferedWorkshopManagerNameContext> attendedWorkshops = new ArrayList<>();

        List<User> users = userRepository.findAll();

        for (WorkshopAttenderInfo workshopAttenderInfo : workshopAttender.getWorkshopAttenderInfos()) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            OfferedWorkshop offeredWorkshop = workshopAttenderInfo.getOfferedWorkshop();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            for (User currentUser : users) {

                WorkshopManager workshopManager = (WorkshopManager) currentUser.getRole("ManagerWorkshopConnection");

                if (workshopManager.getId() == offeredWorkshop.getWorkshopManager().getId()) {
                    offeredWorkshopManagerNameContext.setWorkshopManagerName(currentUser.getName());
                    break;
                }
            }

            attendedWorkshops.add(offeredWorkshopManagerNameContext);

        }

        userHitsoryContext.setAttendedWorkshops(attendedWorkshops);

        Grader grader = (Grader) user.getRole("Grader");

        WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

        List<OfferedWorkshopManagerNameContext> gradedWorkshops = new ArrayList<>();

        for (WorkshopGraderInfo workshopGraderInfo : workshopGrader.getWorkshopGraderInfos()) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            OfferedWorkshop offeredWorkshop = workshopGraderInfo.getOfferedWorkshop();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            for (User currentUser : users) {

                WorkshopManager workshopManager = (WorkshopManager) currentUser.getRole("ManagerWorkshopConnection");

                if (workshopManager.getId() == offeredWorkshop.getWorkshopManager().getId()) {
                    offeredWorkshopManagerNameContext.setWorkshopManagerName(currentUser.getName());
                    break;
                }
            }

            gradedWorkshops.add(offeredWorkshopManagerNameContext);

        }

        userHitsoryContext.setGradedWorkshops(gradedWorkshops);


        WorkshopManager workshopManager = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

        List<OfferedWorkshopManagerNameContext> managedWorkshops = new ArrayList<>();

        for (OfferedWorkshop offeredWorkshop : workshopManager.getOfferedWorkshops()) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();

            offeredWorkshopManagerNameContext.setWorkshopManagerName(user.getName());
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            managedWorkshops.add(offeredWorkshopManagerNameContext);
        }

        userHitsoryContext.setManagedWorkshops(managedWorkshops);

        return new ResponseEntity<>(userHitsoryContext, HttpStatus.OK);


    }



    // Returns User base on Id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


    //Download pic of a given user
    @GetMapping("/profilePic/user/{id}")
    public ResponseEntity<Resource> getUserPic(@PathVariable long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        File pic = user.getPic();

        if (pic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pic.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pic.getFileName() + "\"")
                .body(new ByteArrayResource(pic.getData()));
    }


    @PostMapping("/profilePic/user/{id}")
    public ResponseEntity<Object> setPicForUser(@PathVariable long id, @RequestParam(value = "file", required = true)MultipartFile file) throws IOException {

        Optional<User> optionalUser = userRepository.findById(id);

        if ( !optionalUser.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        File pic = new File();

        pic.setFileName(fileName);
        pic.setData(file.getBytes());
        pic.setFileType(file.getContentType());

        fileRepository.save(pic);

        user.setPic(pic);

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PostMapping("/pic/offeringWorkshop/{id}")
    public ResponseEntity<Object> setPicForOfferingWorkshop(@PathVariable long id, @RequestParam(value = "file") MultipartFile multipartFile) throws IOException {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        File pic = new File();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        pic.setFileType(multipartFile.getContentType());
        pic.setData(multipartFile.getBytes());
        pic.setFileName(fileName);

        offeredWorkshop.setProfileImage(pic);

        fileRepository.save(pic);

        offeringWorkshopRepository.save(offeredWorkshop);

        return new ResponseEntity<>(HttpStatus.OK);


    }


    //Download pic of an offered workshop
    @GetMapping("/pic/offeringWorkshop/{id}")
    public ResponseEntity<Resource> getOfferedWorkshopPic(@PathVariable long id) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        File pic = offeredWorkshop.getProfileImage();

        if (pic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pic.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pic.getFileName() + "\"")
                .body(new ByteArrayResource(pic.getData()));
    }


    @GetMapping("/offeringWorkshop/{id}/info/{userId}")
    public ResponseEntity<Object> getInfoByUserId(@PathVariable long id, @PathVariable long userId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        User user = userRepository.findById(userId).get();

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Grader grader = (Grader) user.getRole("Grader");

        WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

        for (WorkshopGraderInfo workshopGraderInfo : offeredWorkshop.getWorkshopGraderInfos()) {


            if (workshopGraderInfo.getWorkshopGrader().getId() == workshopGrader.getId()) {
                return new ResponseEntity<>(workshopGraderInfo, HttpStatus.OK);
            }
        }


        Attender attender = (Attender) user.getRole("Attender");

        WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();

        for (WorkshopAttenderInfo workshopAttenderInfo : offeredWorkshop.getAttenderInfos()) {


            if (workshopAttenderInfo.getWorkshopAttender().getId() == workshopAttender.getId()) {
                return new ResponseEntity<>(workshopAttenderInfo, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
