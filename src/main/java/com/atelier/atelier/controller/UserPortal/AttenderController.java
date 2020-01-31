package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FileAnswerRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.Request.AttenderRequestPaymentTabRepository;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/attendees")
public class AttenderController {

    private AttenderRepository attenderRepository;
    private UserRepository userRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private WorkshopAttenderInfoRepository workshopAttenderInfoRepository;
    private QuestionRepsoitory questionRepsoitory;
    private AnswerRepository answerRepository;
    private RequestRepository requestRepository;
    private AttenderRequestPaymentTabRepository attenderRequestPaymentTabRepository;
    private FileAnswerRepository fileAnswerRepository;

    public AttenderController(FileAnswerRepository fileAnswerRepository, AttenderRequestPaymentTabRepository attenderRequestPaymentTabRepository, AttenderRepository attenderRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository, RequestRepository requestRepository) {
        this.attenderRepository = attenderRepository;
        this.userRepository = userRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.answerRepository = answerRepository;
        this.requestRepository = requestRepository;
        this.attenderRequestPaymentTabRepository = attenderRequestPaymentTabRepository;
        this.fileAnswerRepository = fileAnswerRepository;
    }

    @GetMapping("/attendee")
    public ResponseEntity<Object> getAttendeeRole(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The attendee role was not found for the user", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        return new ResponseEntity<>(attenderRole, HttpStatus.OK);
    }

    @GetMapping("/attendee/workshops")
    public ResponseEntity<Object> getAttenderWorkshops(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("Attender");
        if (role == null) {
            return new ResponseEntity<>("The user has no attendee role.", HttpStatus.NO_CONTENT);
        }
        Attender attenderRole = (Attender) role;
        AttenderWorkshopConnection attenderWorkshopConnection = attenderRole.getAttenderWorkshopConnection();
        if (attenderWorkshopConnection == null) {
            return new ResponseEntity<>("The user has no attendee workshop connection.", HttpStatus.NO_CONTENT);
        }
        List<WorkshopAttenderInfo> workshopAttenderInfos = attenderWorkshopConnection.getWorkshopAttenderInfos();
        if (workshopAttenderInfos.isEmpty()) {
            return new ResponseEntity<>("The user has no attendee workshop attendee info", HttpStatus.NO_CONTENT);
        }
        List<OfferedWorkshopManagerNameContext> offeredWorkshopManagerNameContexts = new ArrayList<OfferedWorkshopManagerNameContext>();
        List<User> users = userRepository.findAll();

        for (WorkshopAttenderInfo workshopAttenderInfo : workshopAttenderInfos) {

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            OfferedWorkshop offeredWorkshop = workshopAttenderInfo.getOfferedWorkshop();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);

            List<String> managerNames = new ArrayList<>();

            for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()){

                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                for(User currentUser : users){

                    WorkshopManager workshopManager1 = (WorkshopManager) currentUser.getRole("ManagerWorkshopConnection");

                    if (workshopManager.getId() == workshopManager1.getId()){
                        managerNames.add(currentUser.getName());
                        break;
                    }
                }
            }

            offeredWorkshopManagerNameContext.setWorkshopManagers(managerNames);

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
    }


    @GetMapping("/attendee/request/offeringWorkshop/{id}")
    public ResponseEntity<Object> getAttendeeRegisterForm(@PathVariable long id) {
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getAttenderRegisterForm(), HttpStatus.OK);

    }


    @PostMapping("/attendee/request/offeringWorkshop/{id}/answer")
    public ResponseEntity<Object> answerAttenderRegisterForm(@PathVariable long id, Authentication authentication, @RequestBody RegisterRequestContext registerRequestContext, @RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        AttenderWorkshopConnection attenderWorkshopConnection = getAttendeeWorkshopConnectionFromAuthentication(authentication);
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (!offeredWorkshop.hasAtendeeRequested(attenderWorkshopConnection)) {
            return new ResponseEntity<>("The attendee is already in this workshop", HttpStatus.FORBIDDEN);
        }

        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        WorkshopManagerInfo workshopManagerInfo = findWorkshopManagerInfoOfWorkshop(offeredWorkshop, managerWorkshopConnection);
        if (workshopManagerInfo != null) {
            return new ResponseEntity<>("You are the workshop manager", HttpStatus.FORBIDDEN);
        }

        GraderWorkshopConnection graderWorkshopConnection = ((Grader) user.getRole("Grader")).getGraderWorkshopConnection();

        if (!offeredWorkshop.hasGraderRequested(graderWorkshopConnection)) {
            return new ResponseEntity<>("You are the grader of this workshop", HttpStatus.FORBIDDEN);
        }

        for (WorkshopAttenderInfo workshopAttenderInfo : attenderWorkshopConnection.getWorkshopAttenderInfos()) {
            if (OfferedWorkshop.doTwoOfferedWorkshopTimeIntervalsOverlap(workshopAttenderInfo.getOfferedWorkshop(), offeredWorkshop)) {
                return new ResponseEntity<>("Attender' workshop overlaps ", HttpStatus.BAD_REQUEST);
            }
        }

        List<OfferedWorkshopRelationDetail> offeredWorkshopRelationDetails = offeredWorkshop.getWorkshopRelationDetails();
        for (OfferedWorkshopRelationDetail offeredWorkshopRelationDetail : offeredWorkshopRelationDetails) {
            if (!attenderWorkshopConnection.hasPassedWorkshop(offeredWorkshopRelationDetail.getWorkshop())) {
                return new ResponseEntity<>("This attendee hasn't passed the prerequisite of this workshop", HttpStatus.FORBIDDEN);
            }
        }
        AttenderRegisterForm attenderRegisterForm = offeredWorkshop.getAttenderRegisterForm();

        AttenderFormApplicant attenderFormApplicant = new AttenderFormApplicant();
        attenderFormApplicant.setWorkshopAttender(attenderWorkshopConnection);
        attenderWorkshopConnection.addAttenderFormApplicants(attenderFormApplicant);

        List<Answer> answers = new ArrayList<>();

        for (AnswerQuestionContext answerQuestionContext : registerRequestContext.getAnswerQuestionContexts()) {
            Optional<Question> optionalQuestion = questionRepsoitory.findById(answerQuestionContext.getQuestionId());

            if (!optionalQuestion.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Question question = optionalQuestion.get();

            if (question.getForm().getId() != attenderRegisterForm.getId()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Answer answer = new Answer();

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
            } else if (type.equalsIgnoreCase("FileAnswer")){
                FileAnswer fileAnswer = new FileAnswer();
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                fileAnswer.setFileName(fileName);
                fileAnswer.setFileType(multipartFile.getContentType());
                fileAnswer.setData(multipartFile.getBytes());

                answerData = fileAnswer;

//                // TODO ADDED FILE STUFF HERE
//                fileAnswerRepository.save(fileAnswer);

            } else {
                return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
            }




            answer.addAnswerData(answerData);

            answer.setFormApplicant(attenderFormApplicant);
            attenderFormApplicant.addAnswers(answer);

            question.addAnswer(answer);
            answer.setQuestion(question);

            answers.add(answer);

        }
        for (Answer answer : answers) {
            answerRepository.save(answer);
        }
        Request request = new Request();
        request.setRequestable(offeredWorkshop);
        offeredWorkshop.addRequest(request);
        Attender attender = attenderWorkshopConnection.getAttender();
        request.setRequester(attender);
        attender.addRequest(request);
        request.addRequestData(attenderRegisterForm);
        request.setState(RequestState.Pending);
        requestRepository.save(request);
        return new ResponseEntity<>(request.getId(), HttpStatus.OK);
    }

    @PostMapping("/attendee/request/{id}/payments")
    public ResponseEntity<Object> addPayments(@PathVariable long id, @RequestBody PaymentRequestContext paymentRequestContext, Authentication authentication) {

        User user = User.getUser(authentication, userRepository);
        Attender attender = (Attender) user.getRole("Attender");

        Optional<Request> optionalRequest = requestRepository.findById(id);

        if (!optionalRequest.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        Request request = optionalRequest.get();

        if (request.getRequester().getId() != attender.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (request.getRequestData().size() > 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OfferedWorkshop offeredWorkshop = (OfferedWorkshop) request.getRequestable();
        BigDecimal workshopPrice = offeredWorkshop.getPrice();
        List<AttenderPaymentTab> attenderPaymentTabs = new ArrayList<>();
        AttenderRequestPaymentTab attenderRequestPaymentTab = new AttenderRequestPaymentTab();

        if (paymentRequestContext.getType().equalsIgnoreCase("Cash")) {

            PaymentElementRequest paymentElementRequest = paymentRequestContext.getPayments().get(0);

            AttenderPaymentTab attenderPaymentTab = new AttenderPaymentTab();

            try {
                BigDecimal price = new BigDecimal(paymentElementRequest.getAmount());

                if (workshopPrice.compareTo(price) != 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                attenderPaymentTab.setValue(price);
                attenderPaymentTab.setPaid(false);

                String date = paymentElementRequest.getDueDate();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                cal.setTime(dateFormat.parse(date));

                attenderPaymentTab.setPaymentDate(cal);
                attenderPaymentTab.setAttenderRequestPaymentTab(attenderRequestPaymentTab);
                attenderRequestPaymentTab.addPayment(attenderPaymentTab);
            }
            // TODO CHECK FOR THE TYPE OF EXCEPTION
            catch (IllegalArgumentException | ParseException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } else if (paymentRequestContext.getType().equalsIgnoreCase("Installment")) {
            BigDecimal total = new BigDecimal("0");
            for (PaymentElementRequest paymentElementRequest : paymentRequestContext.getPayments()) {
                AttenderPaymentTab attenderPaymentTab = new AttenderPaymentTab();

                try {
                    BigDecimal price = new BigDecimal(paymentElementRequest.getAmount());

                    attenderPaymentTab.setValue(price);
                    attenderPaymentTab.setPaid(false);

                    String date = paymentElementRequest.getDueDate();
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                    cal.setTime(dateFormat.parse(date));

                    attenderPaymentTab.setPaymentDate(cal);
                    attenderPaymentTab.setAttenderRequestPaymentTab(attenderRequestPaymentTab);
                    attenderRequestPaymentTab.addPayment(attenderPaymentTab);
                    total = total.add(price);
                }
                // TODO CHECK FOR THE TYPE OF EXCEPTION
                catch (IllegalArgumentException | ParseException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            if (workshopPrice.compareTo(total) != 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        attenderRequestPaymentTabRepository.save(attenderRequestPaymentTab);

        request.addRequestData(attenderRequestPaymentTab);

        requestRepository.save(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/attendee/request/{id}/payments")
    public ResponseEntity<Object> getRequestPayments(@PathVariable long id, Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Attender attender = (Attender) user.getRole("Attender");

        Optional<Request> optionalRequest = requestRepository.findById(id);

        if (!optionalRequest.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Request request = optionalRequest.get();

        if (request.getRequester().getId() != attender.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (request.getRequestData().size() < 1+2) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AttenderRequestPaymentTab attenderRequestPaymentTab = (AttenderRequestPaymentTab) request.getRequestData().get(1);
        return new ResponseEntity<>(attenderRequestPaymentTab.getAttenderPaymentTabList(), HttpStatus.OK);
    }


    // Returns the attendee groupName, and graders and attendee users of the same group as this user
    @GetMapping("/offeringWorkshop/{id}/groupDetails")
    public ResponseEntity<Object> showUsersOfTheSameGroupAsThisUser(@PathVariable long id, Authentication authentication){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>("The offering workshop with the id provided is not available", HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        AttenderWorkshopConnection attenderWorkshopConnection = getAttendeeWorkshopConnectionFromAuthentication(authentication);
        WorkshopAttenderInfo workshopAttenderInfo = attenderWorkshopConnection.getWorkshopAttenderInfoForAnOfferingWorkshop(offeredWorkshop);
        if (workshopAttenderInfo == null) {
            return new ResponseEntity<>("You aren't an attendee of this workshop", HttpStatus.FORBIDDEN);
        }

        WorkshopGroup attendeeGroup = workshopAttenderInfo.getWorkshopGroup();

        GroupUsersContext groupUsersContext = new GroupUsersContext();

        groupUsersContext.setGroupName(attendeeGroup.getName());
        groupUsersContext.setGroupId(attendeeGroup.getId());

        List<User> users = userRepository.findAll();

        List<User> attendeeUsers = new ArrayList<User>();

        for (WorkshopAttenderInfo attInfo : attendeeGroup.getAttenderInfos()){
            for (User user : users ){
                Attender attender = (Attender) user.getRole("Attender");
                if (attender.getAttenderWorkshopConnection().getId() == attInfo.getWorkshopAttender().getId()){
                    attendeeUsers.add(user);
                    break;
                }
            }
        }

        groupUsersContext.setAttendees(attendeeUsers);

        List<User> graderUsers = new ArrayList<User>();
        for (WorkshopGraderInfo workshopGraderInfo1 : attendeeGroup.getGraderInfos()){
            for(User user : users){
                Grader grader = (Grader) user.getRole("Grader");
                if (grader.getGraderWorkshopConnection().getId() == workshopGraderInfo1.getWorkshopGrader().getId()){
                    graderUsers.add(user);
                    break;
                }
            }
        }

        groupUsersContext.setGraders(graderUsers);

        return new ResponseEntity<>(groupUsersContext, HttpStatus.OK);
    }



    //TODO api to give all the requests of an attendee

    private AttenderWorkshopConnection getAttendeeWorkshopConnectionFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Attender attender = (Attender) user.getRole("Attender");
        return attender.getAttenderWorkshopConnection();
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
