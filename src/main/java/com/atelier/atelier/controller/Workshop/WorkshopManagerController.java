package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.MessagingSystem.Chatroom;
import com.atelier.atelier.entity.MessagingSystem.Chatter;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.RequestService.Requester;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.ChatService.ChatroomRepository;
import com.atelier.atelier.repository.ChatService.ChatterRepository;
import com.atelier.atelier.repository.Form.*;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.Request.RequestableRepository;
import com.atelier.atelier.repository.Request.RequesterRepository;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.role.GraderRepository;
import com.atelier.atelier.repository.user.FileRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/workshopManagers")
public class WorkshopManagerController {

    private WorkshopRepository workshopRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private UserRepository userRepository;
    private GraderEvaluationFormRepository graderEvaluationFormFormRepository;
    private WorkshopFormRepository workshopFormFormRepository;
    private QuestionRepsoitory questionRepsoitory;
    private WorkshopGraderInfoRepository workshopGraderInfoRepository;
    private AnswerRepository answerRepository;
    private RequestRepository requestRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;
    private WorkshopGroupRepository workshopGroupRepository;
    private FormRepository formRepository;
    private GraderRequestFormRepository graderRequestFormRepository;
    private AttenderRegisterFormRepository attenderRegisterFormRepository;
    private RequesterRepository requesterRepository;
    private GraderRepository graderRepository;
    private AttenderRepository attenderRepository;
    private WorkshopFileRepository workshopFileRepository;
    private FileRepository fileRepository;
    private WorkshopManagerInfoRepository workshopManagerInfoRepository;
    private RequestableRepository requestableRepository;
    private ChatroomRepository chatroomRepository;
    private ChatterRepository chatterRepository;
    private OfferedWorkshopInstallmentRepository offeredWorkshopInstallmentRepository;


    public WorkshopManagerController(OfferedWorkshopInstallmentRepository offeredWorkshopInstallmentRepository, ChatterRepository chatterRepository, ChatroomRepository chatroomRepository, RequestableRepository requestableRepository, WorkshopManagerInfoRepository workshopManagerInfoRepository, WorkshopFileRepository workshopFileRepository, FileRepository fileRepository, AttenderRepository attenderRepository, GraderRepository graderRepository, FileAnswerRepository fileAnswerRepository, WorkshopGroupRepository workshopGroupRepository, RequesterRepository requesterRepository, GraderRequestFormRepository graderRequestFormRepository, AttenderRegisterFormRepository attenderRegisterFormRepository, WorkshopFormRepository workshopFormFormRepository, GraderEvaluationFormRepository graderEvaluationFormFormRepository, RequestRepository requestRepository, WorkshopRepository workshopRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory, WorkshopGraderInfoRepository workshopGraderInfoRepository, AnswerRepository answerRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository) {
        this.workshopRepository = workshopRepository;
        this.offeredWorkshopInstallmentRepository = offeredWorkshopInstallmentRepository;
        this.chatroomRepository = chatroomRepository;
        this.chatterRepository = chatterRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.graderEvaluationFormFormRepository = graderEvaluationFormFormRepository;
        this.workshopFormFormRepository = workshopFormFormRepository;
        this.formRepository = formRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
        this.answerRepository = answerRepository;
        this.requestRepository = requestRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.workshopGroupRepository = workshopGroupRepository;
        this.graderRequestFormRepository = graderRequestFormRepository;
        this.attenderRegisterFormRepository = attenderRegisterFormRepository;
        this.requesterRepository = requesterRepository;
        this.graderRepository = graderRepository;
        this.workshopFileRepository = workshopFileRepository;
        this.fileRepository = fileRepository;
        this.attenderRepository = attenderRepository;
        this.requestableRepository = requestableRepository;
        this.workshopManagerInfoRepository = workshopManagerInfoRepository;
    }

    // Returns OfferedWorkshopManagerNameContext Objects of the Manager's Workshops
    @GetMapping("/offeringWorkshop")
    public ResponseEntity<Object> showAllOfferedWorkshop(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("ManagerWorkshopConnection");
        if (role == null) {
            return new ResponseEntity<>("User has no workshop manager role", HttpStatus.NO_CONTENT);
        }
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) role;

        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();

        List<OfferedWorkshop> offeredWorkshops = getManagersOfferedWorkshops(managerWorkshopConnection);
        List<User> users = userRepository.findAll();
        for (OfferedWorkshop offeredWorkshop : offeredWorkshops) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);
            List<String> managerNames = new ArrayList<>();
            for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {
                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                for (User user1 : users) {

                    WorkshopManager workshopManagerRole = (WorkshopManager) user.getRole("ManagerWorkshopConnection");

                    if (workshopManagerRole.getId() == workshopManager.getId()) {
                        managerNames.add(user1.getName());
                        break;
                    }

                }

            }
            offeredWorkshopManagerNameContext.setWorkshopManagers(managerNames);

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
    }



    @PutMapping("/offeringWorkshop/{offeringWorkshopId}")
    public ResponseEntity<Object> editOfferingWorkshop(@PathVariable long offeringWorkshopId, @RequestBody WorkshopEditContext workshopEditContext) throws ParseException {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        offeredWorkshop.setName(workshopEditContext.getName());


        String start = workshopEditContext.getStartTime();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        cal.setTime(dateFormat.parse(start));

        String end = workshopEditContext.getEndTime();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        cal2.setTime(dateFormat1.parse(end));

        offeredWorkshop.setStartTime(cal);
        offeredWorkshop.setEndTime(cal2);

        offeredWorkshop.setDescription(workshopEditContext.getDescription());

        offeredWorkshop.setCashPrice(workshopEditContext.getCashPrice());
        offeredWorkshop.setInstallmentPrice(workshopEditContext.getInstallmentPrice());

        offeringWorkshopRepository.save(offeredWorkshop);

        return new ResponseEntity<>(offeredWorkshop.getId(), HttpStatus.OK);

    }


    @PostMapping("/offeringWorkshop")
    public ResponseEntity<Object> addOfferingWorkshop(@RequestBody OfferingWorkshopContext offeringWorkshopContext) throws ParseException {

        List<ManagerWorkshopConnection> managerWorkshopConnections = new ArrayList<ManagerWorkshopConnection>();
        for (long id : offeringWorkshopContext.getUserManagerId()) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            User user = optionalUser.get();
            ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
            managerWorkshopConnections.add(managerWorkshopConnection);
        }

        long wid = offeringWorkshopContext.getWorkshopId();
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(wid);
        if (!optionalWorkshop.isPresent()) {
            return new ResponseEntity<>("The workshop with the id provided does not exist. ", HttpStatus.NO_CONTENT);
        }
        Workshop workshop = optionalWorkshop.get();
        OfferedWorkshop offeredWorkshop = offeringWorkshopContext.getOfferedWorkshop();

        String start = offeringWorkshopContext.getStartTime();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        cal.setTime(dateFormat.parse(start));

        String end = offeringWorkshopContext.getEndTime();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        cal2.setTime(dateFormat1.parse(end));

        offeredWorkshop.setStartTime(cal);
        offeredWorkshop.setEndTime(cal2);

        if (offeredWorkshop.getStartTime().after(offeredWorkshop.getEndTime())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        offeredWorkshop.setWorkshop(workshop);
        workshop.addOfferingWorkshop(offeredWorkshop); // BI DIRECTIONAL MAPPING
        List<WorkshopManagerInfo> workshopManagerInfos = new ArrayList<>();
        for (WorkshopManager workshopManager : managerWorkshopConnections) {
            WorkshopManagerInfo workshopManagerInfo = new WorkshopManagerInfo();
            workshopManagerInfo.setOfferedWorkshop(offeredWorkshop);
            workshopManagerInfo.setWorkshopManager(workshopManager);
            workshopManager.addWorkshopManagerInfo(workshopManagerInfo);
            workshopManagerInfoRepository.save(workshopManagerInfo);

            workshopManagerInfos.add(workshopManagerInfo);
        }
        offeredWorkshop.setWorkshopManagerInfos(workshopManagerInfos);
        List<Workshop> workshops = new ArrayList<>();
        if (offeringWorkshopContext.getPreRequisiteId() != null) {

            for (Long id : offeringWorkshopContext.getPreRequisiteId()) {
                Optional<Workshop> optionalWorkshop1 = workshopRepository.findById(id);
                if (!optionalWorkshop1.isPresent()) {
                    return new ResponseEntity<>("Workshop not found!", HttpStatus.BAD_REQUEST);
                }
                workshops.add(optionalWorkshop1.get());
            }
            for (Workshop workshop1 : workshops) {
                OfferedWorkshopRelationDetail offeredWorkshopRelationDetail = new OfferedWorkshopRelationDetail();
                offeredWorkshopRelationDetail.setWorkshop(workshop1);
                offeredWorkshopRelationDetail.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshopRelationDetail.setDependencyType(DependencyType.PREREQUISITE);
                workshop1.addOfferingWorkshopRelation(offeredWorkshopRelationDetail);
                offeredWorkshop.addOfferingWorkshopRelations(offeredWorkshopRelationDetail);
            }
        }

        List<User> users = userRepository.findAll();
        List<User> managerUsers = new ArrayList<>();

        for(WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()){
            WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();
            for (User user : users){
                WorkshopManager workshopManager1 = (WorkshopManager) user.getRole("ManagerWorkshopConnection");
                if (workshopManager1.getId() == workshopManager.getId()){
                    managerUsers.add(user);
                    break;
                }
            }
        }

        OfferedWorkshopChatroom attRoom = new OfferedWorkshopChatroom();
        attRoom.setOfferedWorkshop(offeredWorkshop);
        attRoom.setName("Attendees' Chatroom");
        offeredWorkshop.setAttendeesChatroom(attRoom);

        chatroomRepository.save(attRoom);

        OfferedWorkshopChatroom graderRoom = new OfferedWorkshopChatroom();
        graderRoom.setOfferedWorkshop(offeredWorkshop);
        graderRoom.setName("Assistants' Chatroom");
        offeredWorkshop.setGradersChatroom(graderRoom);

        chatroomRepository.save(graderRoom);

        for (User user : managerUsers){
            Chatter chatter = user.getUserChatterConnection();
            attRoom.addChatter(chatter);
            chatter.addChatroom(attRoom);
            chatterRepository.save(chatter);
            chatroomRepository.save(attRoom);

            graderRoom.addChatter(chatter);
            chatter.addChatroom(graderRoom);
            chatterRepository.save(chatter);
            chatroomRepository.save(graderRoom);
        }

        offeringWorkshopRepository.save(offeredWorkshop);
        return new ResponseEntity<>(offeredWorkshop.getId(), HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop/{offeringWorkshopId}/installments")
    public ResponseEntity<Object> addInstallmentPaymentsForOfferingWorkshop(@PathVariable long offeringWorkshopId, @RequestBody PaymentRequestContext paymentRequestContext){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getOfferedWorkshopInstallments() == null || !offeredWorkshop.getOfferedWorkshopInstallments().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<OfferedWorkshopInstallment> offeredWorkshopInstallments = new ArrayList<>();

        BigDecimal total = new BigDecimal("0");
        for (PaymentElementRequest paymentElementRequest : paymentRequestContext.getPayments()) {

            OfferedWorkshopInstallment offeredWorkshopInstallment = new OfferedWorkshopInstallment();

            try {
                BigDecimal price = new BigDecimal(paymentElementRequest.getAmount());

                offeredWorkshopInstallment.setValue(price);

                String date = paymentElementRequest.getDueDate();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                cal.setTime(dateFormat.parse(date));

                offeredWorkshopInstallment.setPaymentDate(cal);
                offeredWorkshopInstallment.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshopInstallmentRepository.save(offeredWorkshopInstallment);
                offeredWorkshopInstallments.add(offeredWorkshopInstallment);
                total = total.add(price);
            }
            catch (IllegalArgumentException | ParseException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (offeredWorkshop.getInstallmentPrice().compareTo(total) != 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        offeredWorkshop.setOfferedWorkshopInstallments(offeredWorkshopInstallments);

        offeringWorkshopRepository.save(offeredWorkshop);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/offeringWorkshop/{id}")
    public ResponseEntity<Object> getOfferingWorkshop(Authentication authentication, @PathVariable Long id) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                return new ResponseEntity<>(offeredWorkshop, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/offeringWorkshop/{id}/graderEvaluationForm")
    public ResponseEntity<Object> getGraderEvalForm(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                GraderEvaluationForm graderEvaluationForm = offeredWorkshop.getGraderEvaluationForm();
                if (graderEvaluationForm == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(graderEvaluationForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/offeringWorkshop/{id}/workshopForms")
    public ResponseEntity<Object> getAllWorkshopForms(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                List<WorkshopForm> workshopForm = offeredWorkshop.getWorkshopForms();
                if (workshopForm.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(workshopForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    @PostMapping("/offeringWorkshop/{id}/workshopForms")
    public ResponseEntity<Object> addAWorkshopForm(@PathVariable long id, Authentication authentication, @RequestBody WorkshopForm workshopForm) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                workshopForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.addWorkshopForm(workshopForm);
                try {
                    workshopFormFormRepository.save(workshopForm);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(workshopForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/offeringWorkshop/{id}/graderEvaluationForm")
    public ResponseEntity<Object> makeGraderEvalForm(@PathVariable long id, Authentication authentication, @RequestBody GraderEvaluationForm graderEvaluationForm) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                graderEvaluationForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setGraderEvaluationForm(graderEvaluationForm);
                try {
                    graderEvaluationFormFormRepository.save(graderEvaluationForm);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(graderEvaluationForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/offeringWorkshop/{id}/attenderRegisterForm")
    public ResponseEntity<Object> makeAttenderRegisterForm(@PathVariable long id, Authentication authentication, @RequestBody AttenderRegisterForm attenderRegisterForm) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                attenderRegisterForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setAttenderRegisterForm(attenderRegisterForm);
                try {
                    attenderRegisterFormRepository.save(attenderRegisterForm);
                    offeringWorkshopRepository.save(offeredWorkshop);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(attenderRegisterForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/offeringWorkshop/{id}/attenderRegisterForm")
    public ResponseEntity<Object> getAttenderRegisterForm(@PathVariable long id, Authentication authentication) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (optionalOfferedWorkshop.isPresent()) {
            OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
            AttenderRegisterForm attenderRegisterForm = offeredWorkshop.getAttenderRegisterForm();
            if (attenderRegisterForm == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(attenderRegisterForm, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/offeringWorkshop/{id}/graderRequestForm")
    public ResponseEntity<Object> getGraderRequestForm(@PathVariable long id, Authentication authentication) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (optionalOfferedWorkshop.isPresent()) {
            OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
            GraderRequestForm graderRequestForm = offeredWorkshop.getGraderRequestForm();
            if (graderRequestForm == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(graderRequestForm, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @PostMapping("/offeringWorkshop/{id}/graderRequestForm")
    public ResponseEntity<Object> makeGraderRequestForm(@PathVariable long id, Authentication authentication, @RequestBody GraderRequestForm graderRequestForm) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
            if (workshopManagerInfo != null) {
                graderRequestForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setGraderRequestForm(graderRequestForm);
                try {
                    graderRequestFormRepository.save(graderRequestForm);
                    offeringWorkshopRepository.save(offeredWorkshop);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(graderRequestForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    @PostMapping("/offeringWorkshop/{id}/graderEvaluationForm/answer")
    public ResponseEntity<Object> answerToGraderEvalForm(@PathVariable long id, @RequestBody FormAnswerContext formAnswerContext, Authentication authentication, @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            Optional<GraderEvaluationForm> optionalForm = graderEvaluationFormFormRepository.findById(formAnswerContext.getFormId());
            if (!optionalForm.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Form form = optionalForm.get();
            Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(formAnswerContext.getApplicantId());

            if (!optionalWorkshopGraderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();

            if (!(offeredWorkshop.getWorkshopGraderInfos().contains(workshopGraderInfo))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);

            if (workshopManagerInfo != null) {

                WorkshopGraderFormApplicant workshopGraderFormApplicant = new WorkshopGraderFormApplicant();
                WorkshopManagerFormFiller workshopManagerFormFiller = new WorkshopManagerFormFiller();
                workshopManagerFormFiller.setWorkshopManager(managerWorkshopConnection);
                managerWorkshopConnection.addFormFiller(workshopManagerFormFiller);
                workshopGraderFormApplicant.setWorkshopGraderInfo(workshopGraderInfo);
                workshopGraderInfo.addWorkshopGraderFormApplicants(workshopGraderFormApplicant);

                List<Answer> answers = new ArrayList<>();

                for (AnswerQuestionContext answerQuestionContext : formAnswerContext.getAnswerQuestion()) {


                    Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());
                    if (!optionalQuestion.isPresent()) {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    }
                    Question question = optionalQuestion.get();
                    FilledAnswer filledAnswer = new FilledAnswer();
                    if (question.getForm().getId() != form.getId()) {
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }

                    String type = answerQuestionContext.getType();
                    LinkedHashMap<String, Object> answerDataObject = answerQuestionContext.getAnswerData();
                    AnswerData answerData = null;

                    if (type.equalsIgnoreCase("TextAnswer")) {
                        TextAnswer textAnswer = new TextAnswer();
                        textAnswer.setText((String) answerDataObject.get("text"));
                        answerData = textAnswer;
                    } else if (type.equalsIgnoreCase("ChoiceAnswer")) {
                        ChoiceAnswer choiceAnswer = new ChoiceAnswer();
                        choiceAnswer.setChoice((Integer) answerDataObject.get("choice"));
                        answerData = choiceAnswer;
                    } else if (type.equalsIgnoreCase("FileAnswer")) {
                        FileAnswer fileAnswer = new FileAnswer();
                        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                        fileAnswer.setFileName(fileName);
                        fileAnswer.setFileType(multipartFile.getContentType());
                        fileAnswer.setData(multipartFile.getBytes());
                        answerData = fileAnswer;
                    } else {
                        return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
                    }

                    filledAnswer.addAnswerData(answerData);

                    filledAnswer.setFormFiller(workshopManagerFormFiller);
                    workshopManagerFormFiller.addAnswer(filledAnswer);

                    filledAnswer.setFormApplicant(workshopGraderFormApplicant);
                    workshopGraderFormApplicant.addAnswers(filledAnswer);

                    question.addAnswer(filledAnswer);
                    filledAnswer.setQuestion(question);
                    answers.add(filledAnswer);
                }
                for (Answer answer : answers) {
                    answerRepository.save(answer);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/offeringWorkshop/{id}/form/questions")
    public ResponseEntity<Object> addQuestion(@RequestBody FormQuestionContext formQuestionContext, @PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (!offeredWorkshopOptional.isPresent()) {
            return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Form> optionalForm = formRepository.findById(formQuestionContext.getFormId());
        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Form form = optionalForm.get();

        if (form instanceof GraderEvaluationForm) {
            GraderEvaluationForm graderEvaluationForm = (GraderEvaluationForm) form;
            if (graderEvaluationForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else if (form instanceof WorkshopForm) {
            WorkshopForm workshopForm = (WorkshopForm) form;
            if (workshopForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else if (form instanceof AttenderRegisterForm) {
            AttenderRegisterForm attenderRegisterForm = (AttenderRegisterForm) form;
            if (attenderRegisterForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else if (form instanceof GraderRequestForm) {
            GraderRequestForm graderRequestForm = (GraderRequestForm) form;
            if (graderRequestForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Question> questions = formQuestionContext.getQuestion();

            for (Question question : questions) {
            List<Answerable> answerables = question.getAnswerables();
            if (answerables != null) {
                for (Answerable answerable : answerables) {
                    answerable.setQuestion(question);
                }
            }

            question.setForm(form);
            form.addQuestion(question);
            questionRepsoitory.save(question);
        }
        if (form instanceof GraderRequestForm) {
            GraderRequestForm graderRequestForm = (GraderRequestForm) form;
            graderRequestFormRepository.save(graderRequestForm);
        } else if (form instanceof AttenderRegisterForm) {
            AttenderRegisterForm attenderRegisterForm = (AttenderRegisterForm) form;
            attenderRegisterFormRepository.save(attenderRegisterForm);
        } else if (form instanceof WorkshopForm) {
            WorkshopForm workshopForm = (WorkshopForm) form;
            workshopFormFormRepository.save(workshopForm);
        } else if (form instanceof GraderEvaluationForm) {
            GraderEvaluationForm graderEvaluationForm = (GraderEvaluationForm) form;
            graderEvaluationFormFormRepository.save(graderEvaluationForm);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private ManagerWorkshopConnection getMangerFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        return managerWorkshopConnection;
    }


    //Returns Grader Request Objects(Both Accepted and Pending)
    @GetMapping("/offeringWorkshop/{id}/requests/graders")
    public ResponseEntity<Object> getGraderRequests(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Request> requests = new ArrayList<>();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Grader) {
                requests.add(request);
            }
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }


    // Returns Attendee Request Object (Both Accepted and Pending and Rejected)
    @GetMapping("/offeringWorkshop/{id}/requests/attendee")
    public ResponseEntity<Object> getAttendeeRequests(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Request> requests = new ArrayList<>();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Attender) {
                requests.add(request);
            }
        }

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop/form/{id}/result")
    public ResponseEntity<Object> getResultOfASingleFormApplicant(@PathVariable long id, @RequestBody RequesterIdContext requesterId) {

        Optional<Form> optionalForm = formRepository.findById(id);

        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Form form = optionalForm.get();

        Optional<Requester> optionalRequester = requesterRepository.findById(requesterId.getId());

        if (!optionalRequester.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Requester requester = optionalRequester.get();

        if ((form instanceof AttenderRegisterForm) && (requester instanceof Attender)) {

            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for (Question question : questions) {
                for (Answer answer : question.getAnswers()) {
                    AttenderFormApplicant attenderFormApplicant = (AttenderFormApplicant) answer.getFormApplicant();
                    AttenderWorkshopConnection attenderWorkshopConnection = (AttenderWorkshopConnection) attenderFormApplicant.getWorkshopAttender();
                    if (attenderWorkshopConnection.getAttender().getId() == requester.getId()) {
                        FormResultContext formResultContext = new FormResultContext();
                        formResultContext.setQuestion(question);
                        formResultContext.setAnswer(answer);
                        formResultContexts.add(formResultContext);
                    }
                }
            }
            return new ResponseEntity<>(formResultContexts, HttpStatus.OK);

        } else if ((form instanceof GraderRequestForm) && (requester instanceof Grader)) {

            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for (Question question : questions) {
                for (Answer answer : question.getAnswers()) {
                    GraderFormApplicant attenderFormApplicant = (GraderFormApplicant) answer.getFormApplicant();
                    GraderWorkshopConnection attenderWorkshopConnection = (GraderWorkshopConnection) attenderFormApplicant.getWorkshopGrader();
                    if (attenderWorkshopConnection.getGrader().getId() == requester.getId()) {
                        FormResultContext formResultContext = new FormResultContext();
                        formResultContext.setQuestion(question);
                        formResultContext.setAnswer(answer);
                        formResultContexts.add(formResultContext);
                    }
                }
            }
            return new ResponseEntity<>(formResultContexts, HttpStatus.OK);

        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }



    @GetMapping("/offeringWorkshop/{id}/requester/{requesterId}")
    public ResponseEntity<Object> getRequesterRequest(@PathVariable long id, Authentication authentication, @PathVariable long requesterId) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester().getId() == requesterId) {
                ShowRequestContext showRequestContext = new ShowRequestContext();
                showRequestContext.setState(request.getState());
                showRequestContext.setId(request.getId());
                return new ResponseEntity<>(showRequestContext, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/offeringWorkshop/{id}/request")
    public ResponseEntity<Object> setRequestStatus(@PathVariable long id, Authentication authentication, @RequestBody RequestStatusContext requestStatusContext) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();


        Optional<Request> optionalRequest = requestRepository.findById(requestStatusContext.getRequestId());


        if (!optionalRequest.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Request request = optionalRequest.get();

        if (request.getState() == RequestState.Accepted || request.getState() == RequestState.Rejected ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String requestStateString = requestStatusContext.getRequestState();
        RequestState requestState = null;

        if (requestStateString.equalsIgnoreCase("ACCEPTED")) {
            requestState = RequestState.Accepted;
        } else if (requestStateString.equalsIgnoreCase("REJECTED")) {
            requestState = RequestState.Rejected;
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        request.setState(requestState);

        if (request.getState() == RequestState.Accepted) {
            User user = userRepository.findById(requestStatusContext.getUserId()).get();
            Requester requester = request.getRequester();
            if (requester instanceof Attender) {
                Attender attender = (Attender) requester;
                Grader grader = (Grader) user.getRole("grader");
                WorkshopAttenderInfo workshopAttenderInfo = enrollAttendeeWorkshop(attender.getAttenderWorkshopConnection(), offeredWorkshop);
                requestRepository.save(request);
                offeredWorkshop.getRequests().removeIf(req -> req.getRequester().getId() == grader.getId() && req.getState() == RequestState.Pending);
                requestableRepository.save(offeredWorkshop);
                offeringWorkshopRepository.save(offeredWorkshop);
                return new ResponseEntity<>(workshopAttenderInfo.getId(), HttpStatus.OK);

            } else if (requester instanceof Grader) {
                Grader grader = (Grader) requester;
                Attender attendee = (Attender) user.getRole("attender");
                WorkshopGraderInfo workshopGraderInfo = enrollGraderWorkshop(grader.getGraderWorkshopConnection(), offeredWorkshop);
                offeredWorkshop.getRequests().removeIf(req -> req.getRequester().getId() == attendee.getId() && req.getState() == RequestState.Pending);
                requestableRepository.save(offeredWorkshop);
                offeringWorkshopRepository.save(offeredWorkshop);
                requestRepository.save(request);

                return new ResponseEntity<>(workshopGraderInfo.getId(), HttpStatus.OK);
            }
        }
        else if(request.getState() == RequestState.Rejected){
            requestRepository.save(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //Returns group objects of the offering workshop with the workshopInfo and Connection Info Ids
    @GetMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> getGroups(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Set<WorkshopGroup> workshopGroupSet = offeredWorkshop.workshopGroupSet();

        List<GroupElementContext> groupElementContexts = new ArrayList<>();

        for (WorkshopGroup workshopGroup : workshopGroupSet) {
            if (workshopGroup == null) {
                continue;
            }

            GroupElementContext groupElementContext = new GroupElementContext();
            groupElementContext.setId(workshopGroup.getId());
            groupElementContext.setName(workshopGroup.getName());
            for (WorkshopGraderInfo workshopGraderInfo : workshopGroup.getGraderInfos()) {
                GroupMemberContext groupMemberContext = new GroupMemberContext();
                groupMemberContext.setWorkshopInfoId(workshopGraderInfo.getId());
                groupMemberContext.setWorkshopConnectionId(workshopGraderInfo.getWorkshopGrader().getId());
                groupElementContext.addGraderInfo(groupMemberContext);
            }
            for (WorkshopAttenderInfo workshopAttenderInfo : workshopGroup.getAttenderInfos()) {
                GroupMemberContext groupMemberContext = new GroupMemberContext();
                groupMemberContext.setWorkshopInfoId(workshopAttenderInfo.getId());
                groupMemberContext.setWorkshopConnectionId(workshopAttenderInfo.getWorkshopAttender().getId());
                groupElementContext.addAttendeeInfo(groupMemberContext);
            }
            groupElementContexts.add(groupElementContext);
        }

        return new ResponseEntity<>(groupElementContexts, HttpStatus.OK);
    }


    @PutMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> editGroup(@PathVariable long id, Authentication authentication, @RequestBody EditGroupContext editGroupContext) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        if ((editGroupContext.getAttendersId() == null || editGroupContext.getAttendersId().isEmpty()) && (editGroupContext.getGradersId() == null || editGroupContext.getGradersId().isEmpty())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<WorkshopGroup> optionalWorkshopGroup = workshopGroupRepository.findById(editGroupContext.getGroupId());
        if (!optionalWorkshopGroup.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        WorkshopGroup workshopGroup = optionalWorkshopGroup.get();

        for (Long graderId : editGroupContext.getGradersId()) {
            Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(graderId);
            if (!optionalWorkshopGraderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();
            if (workshopGraderInfo.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (workshopGraderInfo.getWorkshopGroup() != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            workshopGroup.addGrader(workshopGraderInfo);
            workshopGraderInfo.setWorkshopGroup(workshopGroup);
        }

        for (Long attenderId : editGroupContext.getAttendersId()) {
            Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(attenderId);
            if (!optionalWorkshopAttenderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();
            if (workshopAttenderInfo.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (workshopAttenderInfo.getWorkshopGroup() != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            workshopAttenderInfo.setWorkshopGroup(workshopGroup);
            workshopGroup.addAttender(workshopAttenderInfo);
        }
        workshopGroupRepository.save(workshopGroup);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> addGroups(@PathVariable long id, Authentication authentication, @RequestBody List<GroupWorkshopContext> groupWorkshopContexts) {


        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        List<WorkshopGroup> workshopGroups = new ArrayList<>();
        for (GroupWorkshopContext groupWorkshopContext : groupWorkshopContexts) {
            if ((groupWorkshopContext.getAttendersId() == null || groupWorkshopContext.getAttendersId().isEmpty()) && (groupWorkshopContext.getGradersId() == null || groupWorkshopContext.getGradersId().isEmpty())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            WorkshopGroup workshopGroup = new WorkshopGroup();
            workshopGroup.setName(groupWorkshopContext.getName());
            for (Long graderId : groupWorkshopContext.getGradersId()) {
                Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(graderId);
                if (!optionalWorkshopGraderInfo.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();
                if (workshopGraderInfo.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                if (workshopGraderInfo.getWorkshopGroup() != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                workshopGroup.addGrader(workshopGraderInfo);
                workshopGraderInfo.setWorkshopGroup(workshopGroup);
            }

            for (Long attenderId : groupWorkshopContext.getAttendersId()) {
                Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(attenderId);
                if (!optionalWorkshopAttenderInfo.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();
                if (workshopAttenderInfo.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                if (workshopAttenderInfo.getWorkshopGroup() != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                workshopAttenderInfo.setWorkshopGroup(workshopGroup);
                workshopGroup.addAttender(workshopAttenderInfo);
            }
            workshopGroups.add(workshopGroup);
        }

        for (WorkshopGroup workshopGroup : workshopGroups) {
            workshopGroupRepository.save(workshopGroup);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/offeringWorkshop/group")
    public ResponseEntity<Object> addAnEmptyGroup(@RequestBody RequestStringContext groupName) {


        WorkshopGroup workshopGroup = new WorkshopGroup();

        List<WorkshopGraderInfo> workshopGraderInfos = new ArrayList<>();

        List<WorkshopAttenderInfo> workshopAttenderInfos = new ArrayList<>();

        workshopGroup.setAttenderInfos(workshopAttenderInfos);
        workshopGroup.setGraderInfos(workshopGraderInfos);


        workshopGroup.setName(groupName.getText());

        workshopGroupRepository.save(workshopGroup);

        return new ResponseEntity<>(workshopGroup, HttpStatus.OK);
    }



    @DeleteMapping("/offeringWorkshop/group/{groupId}")
    public ResponseEntity<Object> deleteGroupWithoutDeletingInfos(@PathVariable long groupId){

        Optional<WorkshopGroup> optionalGroup = workshopGroupRepository.findById(groupId);

        if (!optionalGroup.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGroup workshopGroup = optionalGroup.get();

        for(WorkshopGraderInfo workshopGraderInfo : workshopGroup.getGraderInfos()){
            workshopGraderInfo.setWorkshopGroup(null);
            workshopGraderInfoRepository.save(workshopGraderInfo);
        }

        for(WorkshopAttenderInfo workshopAttenderInfo : workshopGroup.getAttenderInfos()){
            workshopAttenderInfo.setWorkshopGroup(null);
            workshopAttenderInfoRepository.save(workshopAttenderInfo);
        }

        workshopGroupRepository.delete(workshopGroup);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop/group/{groupId}/att")
    public ResponseEntity<Object> addAttToGroup(@PathVariable long groupId, @RequestBody RequesterIdContext attInfo) {

        Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(attInfo.getId());

        if (!optionalWorkshopAttenderInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();

        Optional<WorkshopGroup> optionalWorkshopGroup = workshopGroupRepository.findById(groupId);

        if (!optionalWorkshopGroup.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGroup workshopGroup = optionalWorkshopGroup.get();

        workshopGroup.getAttenderInfos().add(workshopAttenderInfo);

        workshopAttenderInfo.setWorkshopGroup(workshopGroup);

        workshopAttenderInfoRepository.save(workshopAttenderInfo);

        workshopGroupRepository.save(workshopGroup);

        return new ResponseEntity<>(HttpStatus.OK);

    }


    @PostMapping("/offeringWorkshop/group/{groupId}/grader")
    public ResponseEntity<Object> addGraderToGroup(@PathVariable long groupId, @RequestBody RequesterIdContext graderInfo) {

        Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(graderInfo.getId());

        if (!optionalWorkshopGraderInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();

        Optional<WorkshopGroup> optionalWorkshopGroup = workshopGroupRepository.findById(groupId);

        if (!optionalWorkshopGroup.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGroup workshopGroup = optionalWorkshopGroup.get();


        workshopGroup.getGraderInfos().add(workshopGraderInfo);

        workshopGraderInfo.setWorkshopGroup(workshopGroup);

        workshopGraderInfoRepository.save(workshopGraderInfo);
        workshopGroupRepository.save(workshopGroup);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/offeringWorkshop/group/{groupId}/atts")
    public ResponseEntity<Object> addAttendeesToGroup(@PathVariable long groupId, @RequestBody List<RequesterIdContext> attendeeInfos) {

        Optional<WorkshopGroup> optionalWorkshopGroup = workshopGroupRepository.findById(groupId);

        if (!optionalWorkshopGroup.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGroup workshopGroup = optionalWorkshopGroup.get();

        for (RequesterIdContext requesterIdContext : attendeeInfos) {

            Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(requesterIdContext.getId());

            if (!optionalWorkshopAttenderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();

            workshopGroup.getAttenderInfos().add(workshopAttenderInfo);
            workshopAttenderInfo.setWorkshopGroup(workshopGroup);

            workshopAttenderInfoRepository.save(workshopAttenderInfo);
        }

        workshopGroupRepository.save(workshopGroup);

        return new ResponseEntity<>(workshopGroup.getId(), HttpStatus.OK);
    }

    @PostMapping("/offeringWorkshop/group/graders")
    public ResponseEntity<Object> addGradersToGroup(@PathVariable long groupId, @RequestBody List<RequesterIdContext> graderInfos) {

        Optional<WorkshopGroup> optionalWorkshopGroup = workshopGroupRepository.findById(groupId);

        if (!optionalWorkshopGroup.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopGroup workshopGroup = optionalWorkshopGroup.get();

        for (RequesterIdContext requesterIdContext : graderInfos) {

            Optional<WorkshopGraderInfo> optionalWorkshopGraderInfo = workshopGraderInfoRepository.findById(requesterIdContext.getId());

            if (!optionalWorkshopGraderInfo.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            WorkshopGraderInfo workshopGraderInfo = optionalWorkshopGraderInfo.get();

            workshopGroup.getGraderInfos().add(workshopGraderInfo);
            workshopGraderInfo.setWorkshopGroup(workshopGroup);

            workshopGraderInfoRepository.save(workshopGraderInfo);
        }

        workshopGroupRepository.save(workshopGroup);

        return new ResponseEntity<>(workshopGroup.getId(), HttpStatus.OK);
    }

    // Returns Attendee Info Objects of the Offering Workshop
    @GetMapping("/offeringWorkshop/{id}/attendeeInfos")
    public ResponseEntity<Object> showAllAttendeeInfos(@PathVariable long id, Authentication authentication) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getAttenderInfos(), HttpStatus.OK);
    }


    // Returns Grader Info Objects
    @GetMapping("/offeringWorkshop/{id}/graderInfos")
    public ResponseEntity<Object> showAllGraderInfos(@PathVariable long id, Authentication authentication) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getWorkshopGraderInfos(), HttpStatus.OK);
    }


    // Returns a list of contexts including the name of the group and the list of its grader and attendee users
    @GetMapping("/offeringWorkshop/{id}/groupDetails")
    public ResponseEntity<Object> showGroupsWithAllOfItsUsers(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        List<User> users = userRepository.findAll();

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();


        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Set<WorkshopGroup> workshopGroupSet = offeredWorkshop.workshopGroupSet();

        List<GroupUsersContext> groupUsersContexts = new ArrayList<GroupUsersContext>();

        for (WorkshopGroup workshopGroup : workshopGroupSet) {
            if (workshopGroup == null) {
                continue;
            }

            GroupUsersContext groupUsersContext = new GroupUsersContext();
            groupUsersContext.setGroupName(workshopGroup.getName());
            groupUsersContext.setGroupId(workshopGroup.getId());

            List<User> graderUsers = new ArrayList<User>();
            for (WorkshopGraderInfo workshopGraderInfo : workshopGroup.getGraderInfos()) {
                for (User user : users) {
                    Grader grader = (Grader) user.getRole("Grader");
                    if (grader.getGraderWorkshopConnection().getId() == workshopGraderInfo.getWorkshopGrader().getId()) {
                        graderUsers.add(user);
                        break;
                    }
                }
            }

            groupUsersContext.setGraders(graderUsers);

            List<User> attendeeUsers = new ArrayList<User>();
            for (WorkshopAttenderInfo workshopAttenderInfo : workshopGroup.getAttenderInfos()) {
                for (User user : users) {
                    Attender attender = (Attender) user.getRole("Attender");
                    if (attender.getAttenderWorkshopConnection().getId() == workshopAttenderInfo.getWorkshopAttender().getId()) {
                        attendeeUsers.add(user);
                        break;
                    }
                }
            }

            groupUsersContext.setAttendees(attendeeUsers);

            groupUsersContexts.add(groupUsersContext);

        }

        return new ResponseEntity<>(groupUsersContexts, HttpStatus.OK);
    }


    // Returns the Requesting Grader Users With Request status As Pending
    @GetMapping("/offeringWorkshop/{id}/requests/pending/graders")
    public ResponseEntity<Object> showGraderUsersWithPendingRequests(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> graders = new ArrayList<User>();

        List<User> users = userRepository.findAll();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Grader) {
                if (request.getState().equals(RequestState.Pending)) {
                    Grader grader = (Grader) request.getRequester();
                    WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();
                    for (User user : users) {
                        Grader userGrader = (Grader) user.getRole("Grader");
                        if (userGrader.getGraderWorkshopConnection().getId() == workshopGrader.getId()) {
                            graders.add(user);
                            break;
                        }
                    }
                }
            }
        }

        return new ResponseEntity<>(graders, HttpStatus.OK);
    }


    // Returns the Requesting Attendees User Objects with Request status as pending
    @GetMapping("/offeringWorkshop/{id}/requests/pending/attendees")
    public ResponseEntity<Object> showPendingAttendeeRequestsUsers(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> attendees = new ArrayList<User>();

        List<User> users = userRepository.findAll();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Attender) {
                if (request.getState().equals(RequestState.Pending)) {
                    Attender attender = (Attender) request.getRequester();
                    WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();
                    for (User user : users) {
                        Attender userAttender = (Attender) user.getRole("Attender");
                        if (userAttender.getAttenderWorkshopConnection().getId() == workshopAttender.getId()) {
                            attendees.add(user);
                            break;
                        }
                    }
                }
            }
        }

        return new ResponseEntity<>(attendees, HttpStatus.OK);

    }


    // Returns the Requesting Attendees User Objects with Request status as pending
    @GetMapping("/offeringWorkshop/{id}/requests/pending/attPayments")
    public ResponseEntity<Object> showPendingAttendeePayments(@PathVariable long id, Authentication authentication) {


        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();


        List<PaymentNameContext> attendees = new ArrayList<>();
        List<User> users = userRepository.findAll();


        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Attender) {
                if (request.getState().equals(RequestState.Pending)) {
                    PaymentNameContext paymentNameContext = new PaymentNameContext();
                    Attender attender = (Attender) request.getRequester();
                    WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();
                    for (User user : users) {
                        Attender userAttender = (Attender) user.getRole("Attender");
                        if (userAttender.getAttenderWorkshopConnection().getId() == workshopAttender.getId()) {
                            paymentNameContext.setName(user.getName());
                            break;
                        }
                    }
                    AttenderRequestPaymentTab attenderRequestPaymentTab = (AttenderRequestPaymentTab) request.getRequestData().get(1);
                    paymentNameContext.setPays(attenderRequestPaymentTab.getAttenderPaymentTabList());
                    attendees.add(paymentNameContext);
                }
            }
        }

        return new ResponseEntity<>(attendees, HttpStatus.OK);

    }


    @GetMapping("/offeringWorkshop/{id}/graderRequest/{graderId}")
    public ResponseEntity<Object> getRequestByGraderId(@PathVariable long id, @PathVariable long graderId) {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();


        Optional<Grader> optionalGrader = graderRepository.findById(graderId);

        if (!optionalGrader.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Grader grader = optionalGrader.get();

        for (Request request : offeredWorkshop.getRequests()) {

            if (request.getState().equals(RequestState.Pending)) {

                if (request.getRequester().getId() == grader.getId()) {
                    return new ResponseEntity<>(request, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/offeringWorkshop/{id}/attendeeRegister/{attId}")
    public ResponseEntity<Object> getRequestByAttId(@PathVariable long id, @PathVariable long attId) {


        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Optional<Attender> optionalAttender = attenderRepository.findById(id);

        if (!optionalAttender.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        Attender attender = optionalAttender.get();

        for (Request request : offeredWorkshop.getRequests()) {

            if (request.getState().equals(RequestState.Pending)) {

                if (request.getRequester().getId() == attender.getId()) {
                    return new ResponseEntity<>(request, HttpStatus.OK);
                }

            }

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private WorkshopGraderInfo enrollGraderWorkshop(WorkshopGrader workshopGrader, OfferedWorkshop offeredWorkshop) {
        WorkshopGraderInfo workshopGraderInfo = new WorkshopGraderInfo();
        workshopGraderInfo.setWorkshopGrader(workshopGrader);
        workshopGraderInfo.setOfferedWorkshop(offeredWorkshop);
        offeredWorkshop.addWorkshopGraderrInfo(workshopGraderInfo);
        workshopGrader.addGraderInfo(workshopGraderInfo);
        workshopGraderInfoRepository.save(workshopGraderInfo);

        List<User> users = userRepository.findAll();
        OfferedWorkshopChatroom offeredWorkshopChatroom = offeredWorkshop.getGradersChatroom();

        for (User user : users){

            Grader grader = (Grader) user.getRole("Grader");

            if (grader.getGraderWorkshopConnection().getId() == workshopGrader.getId()){
                Chatter chatter = user.getUserChatterConnection();
                offeredWorkshopChatroom.addChatter(chatter);
                chatter.addChatroom(offeredWorkshopChatroom);
                chatterRepository.save(chatter);
                chatroomRepository.save(offeredWorkshopChatroom);
                break;
            }
        }

        return workshopGraderInfo;
    }

    private WorkshopAttenderInfo enrollAttendeeWorkshop(WorkshopAttender workshopAttender, OfferedWorkshop offeredWorkshop) {
        WorkshopAttenderInfo workshopAttenderInfo = new WorkshopAttenderInfo();
        workshopAttenderInfo.setOfferedWorkshop(offeredWorkshop);
        workshopAttenderInfo.setWorkshopAttender(workshopAttender);
        workshopAttender.addWorkshopAttenderInfo(workshopAttenderInfo);
        offeredWorkshop.addWorkshopAttenderInfo(workshopAttenderInfo);
        workshopAttenderInfoRepository.save(workshopAttenderInfo);

        List<User> users = userRepository.findAll();
        OfferedWorkshopChatroom offeredWorkshopChatroom = offeredWorkshop.getAttendeesChatroom();

        for (User user : users){

            Attender attender = (Attender) user.getRole("Attender");

            if (attender.getAttenderWorkshopConnection().getId() == workshopAttender.getId()){
                Chatter chatter = user.getUserChatterConnection();
                offeredWorkshopChatroom.addChatter(chatter);
                chatter.addChatroom(offeredWorkshopChatroom);
                chatterRepository.save(chatter);
                chatroomRepository.save(offeredWorkshopChatroom);
                break;
            }
        }

        return workshopAttenderInfo;
    }


    // API to get the grader infos list of a workshop without a group (User and info Id)
    @GetMapping("/offeringWorkshop/{offeringWorkshopId}/groupless/graderInfos")
    public ResponseEntity<Object> getGraderUsersAndInfosWithoutAGroup(@PathVariable long offeringWorkshopId) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        List<UserInfoContext> userInfoContexts = new ArrayList<UserInfoContext>();
        List<User> users = userRepository.findAll();
        for (WorkshopGraderInfo workshopGraderInfo : offeredWorkshop.getWorkshopGraderInfos()) {

            if (workshopGraderInfo.getWorkshopGroup() == null) {
                UserInfoContext userInfoContext = new UserInfoContext();

                userInfoContext.setInfoId(workshopGraderInfo.getId());

                for (User user : users) {

                    Grader grader = (Grader) user.getRole("Grader");
                    WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();

                    if (workshopGrader.getWorkshopGraderInfos().contains(workshopGraderInfo)) {
                        userInfoContext.setUser(user);
                        break;
                    }
                }
                userInfoContexts.add(userInfoContext);
            }
        }

        return new ResponseEntity<>(userInfoContexts, HttpStatus.OK);
    }

    // API to get the attendee infos list of a workshop without a group
    @GetMapping("/offeringWorkshop/{offeringWorkshopId}/groupless/attendeeInfos")
    public ResponseEntity<Object> getAttendeeUsersAndInfosWithoutAGroup(@PathVariable long offeringWorkshopId) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        List<UserInfoContext> userInfoContexts = new ArrayList<UserInfoContext>();
        List<User> users = userRepository.findAll();
        for (WorkshopAttenderInfo workshopAttenderInfo : offeredWorkshop.getAttenderInfos()) {

            if (workshopAttenderInfo.getWorkshopGroup() == null) {
                UserInfoContext userInfoContext = new UserInfoContext();

                userInfoContext.setInfoId(workshopAttenderInfo.getId());

                for (User user : users) {

                    Attender attender = (Attender) user.getRole("Attender");
                    WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();

                    if (workshopAttender.getWorkshopAttenderInfos().contains(workshopAttenderInfo)) {
                        userInfoContext.setUser(user);
                        break;
                    }
                }
                userInfoContexts.add(userInfoContext);
            }
        }
        return new ResponseEntity<>(userInfoContexts, HttpStatus.OK);
    }


    // API to create workshop file (GET method in WorkshopRestController)
    @PostMapping("/offeringWorkshop/{offeringWorkshopId}/workshopFile/create")
    public ResponseEntity<Object> createWorkshopFile(@PathVariable long offeringWorkshopId, @RequestBody WorkshopFileContext workshopFileContext, Authentication authentication) throws IOException {

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

//        WorkshopManager workshopManager = getMangerFromAuthentication(authentication);

//        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, workshopManager);
//        if (workshopManagerInfo == null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        WorkshopFile workshopFile = new WorkshopFile();

        workshopFile.setTitle(workshopFileContext.getTitle());
        workshopFile.setDescription(workshopFileContext.getDescription());
        workshopFile.setOfferedWorkshop(offeredWorkshop);

        if (workshopFileContext.getType().equalsIgnoreCase("Link")){
            URL url = new URL(workshopFileContext.getLink());
            workshopFile.setUrlLink(url);
            workshopFile.setWorkshopFileType(WorkshopFileType.Link);
        }
        else if (workshopFileContext.getType().equalsIgnoreCase("File")){
            workshopFile.setWorkshopFileType(WorkshopFileType.File);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<WorkshopFileReceiver> workshopFileReceivers = new ArrayList<>();
        for (String receiver : workshopFileContext.getReceiverList()) {

            if (receiver.equalsIgnoreCase("Attendee")) {
                workshopFileReceivers.add(WorkshopFileReceiver.Attendee);
            } else if (receiver.equalsIgnoreCase("Grader")) {
                workshopFileReceivers.add(WorkshopFileReceiver.Grader);
            } else if (receiver.equalsIgnoreCase("Supervisor")) {
                workshopFileReceivers.add(WorkshopFileReceiver.Supervisor);
            }
        }

        workshopFile.setReceivers(workshopFileReceivers);

        offeredWorkshop.addWorkshopFile(workshopFile);

        workshopFileRepository.save(workshopFile);

        offeringWorkshopRepository.save(offeredWorkshop);


        return new ResponseEntity<>(workshopFile.getId(), HttpStatus.CREATED);
    }


    @PostMapping("/offeringWorkshop/workshopFile/{workshopFileId}/upload")
    public ResponseEntity<Object> uploadWorkshopFile(@PathVariable long workshopFileId, @RequestParam(value = "file") MultipartFile multipartFile, Authentication authentication) throws IOException {


        Optional<WorkshopFile> optionalWorkshopFile = workshopFileRepository.findById(workshopFileId);

        if (!optionalWorkshopFile.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        WorkshopFile workshopFile = optionalWorkshopFile.get();


        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        File uploadedFile = new File();

        uploadedFile.setFileName(fileName);
        uploadedFile.setData(multipartFile.getBytes());
        uploadedFile.setFileType(multipartFile.getContentType());

        fileRepository.save(uploadedFile);

        workshopFile.setFile(uploadedFile);

        workshopFileRepository.save(workshopFile);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/workshop/offeringWorkshops/download/")
                .path(Long.toString(uploadedFile.getId()))
                .toUriString();

        return new ResponseEntity<>(fileDownloadUri, HttpStatus.OK);
    }



    public List<OfferedWorkshop> getManagersOfferedWorkshops(WorkshopManager workshopManager) {

        List<OfferedWorkshop> offeredWorkshops = new ArrayList<>();

        for (WorkshopManagerInfo workshopManagerInfo : workshopManager.getWorkshopManagerInfos()) {
            offeredWorkshops.add(workshopManagerInfo.getOfferedWorkshop());
        }

        return offeredWorkshops;
    }


    public WorkshopManagerInfo findWorkshopManagerInfoOfWorkshop(OfferedWorkshop offeredWorkshop, WorkshopManager workshopManager) {

        for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {

            if (workshopManagerInfo.getWorkshopManager().getId() == workshopManager.getId()) {
                return workshopManagerInfo;
            }
        }

        return null;
    }
}
