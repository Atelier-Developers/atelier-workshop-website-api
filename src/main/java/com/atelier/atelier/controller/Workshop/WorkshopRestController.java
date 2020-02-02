package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.Requester;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Request.RequesterRepository;
import com.atelier.atelier.repository.user.FileRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopFileRepository;
import com.atelier.atelier.repository.workshop.WorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    private WorkshopFileRepository workshopFileRepository;
    private FileRepository fileRepository;
    private RequesterRepository requesterRepository;

    @Autowired
    public WorkshopRestController(RequesterRepository requesterRepository, FileRepository fileRepository, WorkshopFileRepository workshopFileRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.workshopFileRepository = workshopFileRepository;
        this.fileRepository = fileRepository;
        this.requesterRepository = requesterRepository;
    }


    @GetMapping("/workshops/offeredWorkshop/{offeredWorkshopId}/count")
    public ResponseEntity<Object> getOfferedWorkshopCount(@PathVariable long offeredWorkshopId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeredWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        int attenderCount = offeredWorkshop.getAttenderInfos().size();

        EnrollCountContext enrollCountContext = new EnrollCountContext();

        enrollCountContext.setCount(attenderCount);

        return new ResponseEntity<>(enrollCountContext, HttpStatus.OK);


    }


    @GetMapping("/workshops/offeredWorkshops/att/{offeredWorkshopId}")
    public ResponseEntity<Object> getAttendeeInfosById(@PathVariable long offeredWorkshopId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeredWorkshopId);

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getAttenderInfos(), HttpStatus.OK);

    }


    @GetMapping("/workshops")
    public ResponseEntity<Object> getAllWorkshops() {
        List<Workshop> workshops = workshopRepository.findAll();
        if (workshops.isEmpty()) {
            return new ResponseEntity<>("No workshops were found", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(workshops, HttpStatus.OK);
    }

    @GetMapping("/workshops/{workshopId}")
    public ResponseEntity<Object> getWorkshopById(@PathVariable long workshopId) {
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshopId);

        if (!optionalWorkshop.isPresent()) {
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop workshop = optionalWorkshop.get();
        return new ResponseEntity<>(workshop, HttpStatus.OK);
    }

    @GetMapping("/workshops/{workshopId}/offeringWorkshop")
    public ResponseEntity<Object> getOfferingWorkshopsOfAWorkshop(@PathVariable long workshopId) {

        Optional<Workshop> optionalWorkshop = workshopRepository.findById(workshopId);

        if (!optionalWorkshop.isPresent()) {
            return new ResponseEntity<>("No workshop with the id provided was found", HttpStatus.NO_CONTENT);
        }

        Workshop workshop = optionalWorkshop.get();

        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();
        List<User> users = userRepository.findAll();

        for (OfferedWorkshop offeredWorkshop : workshop.getOfferedWorkshops()) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();

            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            List<WorkshopManagerInfo> workshopManagerInfos = offeredWorkshop.getWorkshopManagerInfos();

            List<String> workshopManagerUsers = new ArrayList<String>();

            for (WorkshopManagerInfo workshopManagerInfo : workshopManagerInfos) {
                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();
                for (User user : users) {

                    WorkshopManager workshopManager1 = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                    if (workshopManager1.getId() == workshopManager.getId()) {

                        workshopManagerUsers.add(user.getName());
                        break;
                    }

                }
            }

            offeredWorkshopManagerNameContext.setWorkshopManagers(workshopManagerUsers);


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
    public ResponseEntity<Object> showOfferingWorkshopsForHomePage() {

        List<OfferedWorkshop> offeredWorkshops = offeringWorkshopRepository.findAll();
        List<User> users = userRepository.findAll();

        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();

        for (OfferedWorkshop offeredWorkshop : offeredWorkshops) {

            List<String> workshopManagerUsers = new ArrayList<String>();
            for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {
                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                for (User user : users) {

                    WorkshopManager workshopManagerRole = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                    if (workshopManagerRole.getId() == workshopManager.getId()) {
                        workshopManagerUsers.add(user.getName());
                        break;
                    }

                }
            }


            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);
            offeredWorkshopManagerNameContext.setWorkshopManagers(workshopManagerUsers);

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/{id}")
    public ResponseEntity<Object> showSingleOfferingWorkshopByIdForWorkshopPage(@PathVariable long id) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<User> users = userRepository.findAll();

        OfferedWorkshopUserListsContext offeredWorkshopUserListsContext = new OfferedWorkshopUserListsContext();

        offeredWorkshopUserListsContext.setOfferedWorkshop(offeredWorkshop);

        List<User> managerUsers = new ArrayList<User>();

        for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {

            WorkshopManager workshopManager1 = workshopManagerInfo.getWorkshopManager();

            for (User user : users) {

                WorkshopManager workshopManager = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                if (workshopManager.getId() == workshopManager1.getId()) {

                    managerUsers.add(user);
                    break;
                }
            }
        }

        offeredWorkshopUserListsContext.setWorkshopManagerUser(managerUsers);

        List<User> attendeeUsers = new ArrayList<User>();
        for (WorkshopAttenderInfo workshopAttenderInfo : offeredWorkshop.getAttenderInfos()) {

            WorkshopAttender workshopAttender = workshopAttenderInfo.getWorkshopAttender();

            for (User user : users) {

                Attender attender = (Attender) user.getRole("Attender");

                WorkshopAttender workshopAttender1 = attender.getAttenderWorkshopConnection();

                if (workshopAttender1.getId() == workshopAttender.getId()) {

                    attendeeUsers.add(user);

                }
            }
        }

        List<User> graderUsers = new ArrayList<User>();
        for (WorkshopGraderInfo workshopGraderInfo : offeredWorkshop.getWorkshopGraderInfos()) {

            WorkshopGrader workshopGrader = workshopGraderInfo.getWorkshopGrader();

            for (User user : users) {

                Grader grader = (Grader) user.getRole("Grader");

                WorkshopGrader workshopGrader1 = grader.getGraderWorkshopConnection();

                if (workshopGrader1.getId() == workshopGrader.getId()) {

                    graderUsers.add(user);
                }
            }
        }

        List<String> preReqs = new ArrayList<String>();
        for (OfferedWorkshopRelationDetail offeredWorkshopRelationDetail : offeredWorkshop.getWorkshopRelationDetails()) {

            Optional<Workshop> optionalWorkshop = workshopRepository.findById(offeredWorkshopRelationDetail.getWorkshop().getId());

            if (!optionalWorkshop.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Workshop workshop = optionalWorkshop.get();

            preReqs.add(workshop.getName());


        }

        offeredWorkshopUserListsContext.setAttendeeUsers(attendeeUsers);
        offeredWorkshopUserListsContext.setGraderUsers(graderUsers);
        offeredWorkshopUserListsContext.setPreRequisites(preReqs);

        return new ResponseEntity<>(offeredWorkshopUserListsContext, HttpStatus.OK);
    }


    //TODO USER REQUEST STATUS
    @GetMapping("/offeringWorkshops/{offeringWorkshopId}/requestStatus/{userId}")
    public ResponseEntity<Object> getUserRequestStatusAtOfferingWorkshop(@PathVariable long offeringWorkshopId, @PathVariable long userId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();
        Attender attender = (Attender) user.getRole("Attender");
        Grader grader = (Grader) user.getRole("Grader");
        GradAttRequestStatus gradAttRequestStatus = new GradAttRequestStatus();
        for (Request request : offeredWorkshop.getRequests()) {
            Requester requester = request.getRequester();
            if (requester.getId() == attender.getId()) {
                gradAttRequestStatus.setAttReq(request);
            } else if (requester.getId() == grader.getId()) {
                gradAttRequestStatus.setGraderReq(request);
            }
        }
        return new ResponseEntity<>(gradAttRequestStatus, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/popular")
    public ResponseEntity<Object> showFirstFivePopularWorkshops() {

        List<OfferedWorkshop> offeredWorkshops = offeringWorkshopRepository.findAll();
        List<User> users = userRepository.findAll();

        if (offeredWorkshops.size() > 5) {
            List<OfferedWorkshop> result = offeredWorkshops.subList(0, 5);
            Collections.sort(result, Collections.reverseOrder());

            List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<>();

            for (OfferedWorkshop offeredWorkshop : result) {
                OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
                offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

                List<String> managerNames = new ArrayList<>();

                for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {
                    WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                    for (User user : users) {

                        WorkshopManager workshopManager1 = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                        if (workshopManager.getId() == workshopManager1.getId()) {
                            managerNames.add(user.getName());
                            break;
                        }
                    }

                }

                offeredWorkshopManagerNameContext.setWorkshopManagers(managerNames);

                offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
            }

            return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
        } else {

            Collections.sort(offeredWorkshops, Collections.reverseOrder());


            List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<>();

            for (OfferedWorkshop offeredWorkshop : offeredWorkshops) {
                OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
                offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

                List<String> managerNames = new ArrayList<>();

                for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {
                    WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                    for (User user : users) {

                        WorkshopManager workshopManager1 = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                        if (workshopManager1.getId() == workshopManager.getId()) {
                            managerNames.add(user.getName());
                            break;
                        }
                    }
                }

                offeredWorkshopManagerNameContext.setWorkshopManagers(managerNames);


                offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
            }

            return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
        }


    }


    // Get a workshop file's file download URI, title, description, receivers list
    @GetMapping("/offeringWorkshops/workshopFile/{workshopFileId}")
    public ResponseEntity<Object> getDownloadUriForAFile(@PathVariable long workshopFileId) {


        Optional<WorkshopFile> optionalWorkshopFile = workshopFileRepository.findById(workshopFileId);

        if (!optionalWorkshopFile.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopFile workshopFile = optionalWorkshopFile.get();

        WorkshopFileGETContext workshopFileGETContext = new WorkshopFileGETContext();

        workshopFileGETContext.setTitle(workshopFile.getTitle());
        workshopFileGETContext.setDescription(workshopFile.getDescription());

        List<String> workshopFileReceivers = new ArrayList<String>();
        for (WorkshopFileReceiver workshopFileReceiver : workshopFile.getReceivers()) {

            if (workshopFileReceiver.equals(WorkshopFileReceiver.Attendee)) {
                workshopFileReceivers.add("Attendee");
            } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Grader)) {
                workshopFileReceivers.add("Grader");
            } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Manager)) {
                workshopFileReceivers.add("Manager");
            }
        }

        workshopFileGETContext.setReceivers(workshopFileReceivers);

        File file = workshopFile.getFile();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/workshop/offeringWorkshops/download/")
                .path(Long.toString(file.getId()))
                .toUriString();

        workshopFileGETContext.setDownloadURI(fileDownloadUri);

        return new ResponseEntity<>(workshopFileGETContext, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/{offeringWorkshopId}/workshopFiles/attendees")
    public ResponseEntity<Object> getAttendeeWorkshopFiles(@PathVariable long offeringWorkshopId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);


        if (!optionalOfferedWorkshop.isPresent()) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<WorkshopFile> workshopFiles = offeredWorkshop.getWorkshopFiles();

        List<WorkshopFileGETContext> workshopFileGETContexts = new ArrayList<>();

        for (WorkshopFile workshopFile : workshopFiles) {

            if (workshopFile.getReceivers().contains(WorkshopFileReceiver.Attendee)) {


                WorkshopFileGETContext workshopFileGETContext = new WorkshopFileGETContext();

                workshopFileGETContext.setTitle(workshopFile.getTitle());
                workshopFileGETContext.setDescription(workshopFile.getDescription());

                List<String> workshopFileReceivers = new ArrayList<String>();
                for (WorkshopFileReceiver workshopFileReceiver : workshopFile.getReceivers()) {

                    if (workshopFileReceiver.equals(WorkshopFileReceiver.Attendee)) {
                        workshopFileReceivers.add("Attendee");
                    } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Grader)) {
                        workshopFileReceivers.add("Grader");
                    } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Manager)) {
                        workshopFileReceivers.add("Manager");
                    }
                }

                workshopFileGETContext.setReceivers(workshopFileReceivers);

                File file = workshopFile.getFile();

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/workshop/offeringWorkshops/download/")
                        .path(Long.toString(file.getId()))
                        .toUriString();

                workshopFileGETContext.setDownloadURI(fileDownloadUri);

                workshopFileGETContexts.add(workshopFileGETContext);

            }

        }

        return new ResponseEntity<>(workshopFileGETContexts, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/{offeringWorkshopId}/workshopFiles/graders")
    public ResponseEntity<Object> getGraderWorkshopFiles(@PathVariable long offeringWorkshopId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<WorkshopFile> workshopFiles = offeredWorkshop.getWorkshopFiles();

        List<WorkshopFileGETContext> workshopFileGETContexts = new ArrayList<>();
        for (WorkshopFile workshopFile : workshopFiles) {

            if (workshopFile.getReceivers().contains(WorkshopFileReceiver.Grader)) {

                WorkshopFileGETContext workshopFileGETContext = new WorkshopFileGETContext();

                workshopFileGETContext.setTitle(workshopFile.getTitle());
                workshopFileGETContext.setDescription(workshopFile.getDescription());

                List<String> workshopFileReceivers = new ArrayList<String>();
                for (WorkshopFileReceiver workshopFileReceiver : workshopFile.getReceivers()) {

                    if (workshopFileReceiver.equals(WorkshopFileReceiver.Attendee)) {
                        workshopFileReceivers.add("Attendee");
                    } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Grader)) {
                        workshopFileReceivers.add("Grader");
                    } else if (workshopFileReceiver.equals(WorkshopFileReceiver.Manager)) {
                        workshopFileReceivers.add("Manager");
                    }
                }

                workshopFileGETContext.setReceivers(workshopFileReceivers);

                File file = workshopFile.getFile();

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/workshop/offeringWorkshops/download/")
                        .path(Long.toString(file.getId()))
                        .toUriString();

                workshopFileGETContext.setDownloadURI(fileDownloadUri);

                workshopFileGETContexts.add(workshopFileGETContext);
            }
        }

        return new ResponseEntity<>(workshopFileGETContexts, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshops/download/{fileId}")
    public ResponseEntity<Object> downloadWorkshopFile(@PathVariable long fileId) {

        Optional<File> optionalFile = fileRepository.findById(fileId);

        if (!optionalFile.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        File file = optionalFile.get();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }


}
