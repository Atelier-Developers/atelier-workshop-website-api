package com.atelier.atelier.controller.UserPortal;


import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.AnswerRepository;
import com.atelier.atelier.repository.Form.FileAnswerRepository;
import com.atelier.atelier.repository.Form.FormApplicantRepository;
import com.atelier.atelier.repository.Form.QuestionRepsoitory;
import com.atelier.atelier.repository.Request.AttenderRequestPaymentTabRepository;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.role.AttenderRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderInfoRepository;
import com.atelier.atelier.repository.workshop.WorkshopAttenderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


// TODO Unpay,  Get and Delete Personal file, History


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
    private WorkshopAttenderRepository workshopAttenderRepository;
    private FileAnswerRepository fileAnswerRepository;
    private FormApplicantRepository formApplicantRepository;

    public AttenderController(FormApplicantRepository formApplicantRepository, WorkshopAttenderRepository workshopAttenderRepository, FileAnswerRepository fileAnswerRepository, AttenderRequestPaymentTabRepository attenderRequestPaymentTabRepository, AttenderRepository attenderRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository, QuestionRepsoitory questionRepsoitory, AnswerRepository answerRepository, RequestRepository requestRepository) {
        this.attenderRepository = attenderRepository;
        this.formApplicantRepository = formApplicantRepository;
        this.workshopAttenderRepository = workshopAttenderRepository;
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

            for (WorkshopManagerInfo workshopManagerInfo : offeredWorkshop.getWorkshopManagerInfos()) {

                WorkshopManager workshopManager = workshopManagerInfo.getWorkshopManager();

                for (User currentUser : users) {

                    WorkshopManager workshopManager1 = (WorkshopManager) currentUser.getRole("ManagerWorkshopConnection");

                    if (workshopManager.getId() == workshopManager1.getId()) {
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
                return new ResponseEntity<>("Attender' workshop overlaps ", HttpStatus.CONFLICT);
            }
        }

        List<OfferedWorkshopRelationDetail> offeredWorkshopRelationDetails = offeredWorkshop.getWorkshopRelationDetails();
        for (OfferedWorkshopRelationDetail offeredWorkshopRelationDetail : offeredWorkshopRelationDetails) {
            if (!attenderWorkshopConnection.hasPassedWorkshop(offeredWorkshopRelationDetail.getWorkshop())) {
                return new ResponseEntity<>("This attendee hasn't passed the prerequisite of this workshop", HttpStatus.I_AM_A_TEAPOT);
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
        attenderFormApplicant.setRequest(request);
        requestRepository.save(request);
        return new ResponseEntity<>(request.getId(), HttpStatus.OK);
    }


    @DeleteMapping("/attendee/request/offeringWorkshop/{offeringWorkshopId}/request")
    public ResponseEntity<Object> revertAttendeeRegisterRequestForAnOfferingWorkshop(@PathVariable long offeringWorkshopId, Authentication authentication) {

        AttenderWorkshopConnection attenderWorkshopConnection = getAttendeeWorkshopConnectionFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Attender attender = attenderWorkshopConnection.getAttender();
        Request request = null;

        for (Request request1 : offeredWorkshop.getRequests()) {

            if (request1.getRequester().equals(attender) && request1.getState().equals(RequestState.Pending)) {
                request = request1;
                break;
            }

        }

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AttenderRegisterForm attenderRegisterForm = (AttenderRegisterForm) request.getRequestData().get(0);

        List<Question> questions = attenderRegisterForm.getQuestions();

        AttenderFormApplicant attenderFormApplicant = null;

        List<Answer> answers = questions.get(0).getAnswers();
        for (Answer answer : answers) {

                if (attenderWorkshopConnection.getAttenderFormApplicants().contains(answer.getFormApplicant()) && ((AttenderFormApplicant)answer.getFormApplicant()).getRequest().getId() == request.getId()) {
                    attenderFormApplicant = (AttenderFormApplicant) answer.getFormApplicant();
                    break;
                }


        }

        if (attenderFormApplicant == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        for (Answer answer : attenderFormApplicant.getAnswers()) {

            Question answeredQuestion = answer.getQuestion();

            answeredQuestion.getAnswers().remove(answer);

            questionRepsoitory.save(answeredQuestion);

            answer.setQuestion(null);
            answerRepository.save(answer);
        }

        attenderWorkshopConnection.getAttenderFormApplicants().remove(attenderFormApplicant);

        workshopAttenderRepository.save(attenderWorkshopConnection);

        attenderFormApplicant.setWorkshopAttender(null);

        formApplicantRepository.save(attenderFormApplicant);

        formApplicantRepository.delete(attenderFormApplicant);

        offeredWorkshop.getRequests().remove(request);

        offeringWorkshopRepository.save(offeredWorkshop);

        request.setRequestable(null);

        attender.getRequests().remove(request);

        attenderRepository.save(attender);

        request.setRequester(null);

        AttenderRequestPaymentTab attenderRequestPaymentTab = (AttenderRequestPaymentTab) request.getRequestData().get(1);

        attenderRequestPaymentTabRepository.delete(attenderRequestPaymentTab);

        request.setRequestData(null);

        requestRepository.delete(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/attendee/{id}/offeringWorkshop/{offId}/payment")
    public ResponseEntity<Object> getRequestPaymentOfAttendee(@PathVariable long id, @PathVariable long offId) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();
        Attender attender = (Attender) user.getRole("Attender");

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offId);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request.getRequester().getId() == attender.getId() && request.getRequestable().getId() == offeredWorkshop.getId() && request.getState().equals(RequestState.Pending)) {
                return new ResponseEntity<>(request.getRequestData().get(1), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        AttenderRequestPaymentTab attenderRequestPaymentTab = new AttenderRequestPaymentTab();

        if (paymentRequestContext.getType().equalsIgnoreCase("Cash")) {


            AttenderPaymentTab attenderPaymentTab = new AttenderPaymentTab();

            BigDecimal price = offeredWorkshop.getCashPrice();

            attenderPaymentTab.setValue(price);
            attenderPaymentTab.setPaid(false);


            Calendar cal = Calendar.getInstance();
            attenderPaymentTab.setPaymentDate(cal);


            attenderPaymentTab.setAttenderRequestPaymentTab(attenderRequestPaymentTab);
            attenderRequestPaymentTab.addPayment(attenderPaymentTab);


        } else if (paymentRequestContext.getType().equalsIgnoreCase("Installment")) {
//            BigDecimal total = new BigDecimal("0");
            for (OfferedWorkshopInstallment offeredWorkshopInstallment : offeredWorkshop.getOfferedWorkshopInstallments()) {
                AttenderPaymentTab attenderPaymentTab = new AttenderPaymentTab();

//                try {
//                    BigDecimal price = offeredWorkshopInstallment.getValue();

                attenderPaymentTab.setValue(offeredWorkshopInstallment.getValue());
                attenderPaymentTab.setPaid(false);


                attenderPaymentTab.setPaymentDate(offeredWorkshopInstallment.getPaymentDate());
                attenderPaymentTab.setAttenderRequestPaymentTab(attenderRequestPaymentTab);
                attenderRequestPaymentTab.addPayment(attenderPaymentTab);
//                    total = total.add(price);
//                }
//                catch (IllegalArgumentException | ParseException e) {
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
            }
//            if (installmentPrice.compareTo(total) != 0) {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }

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
        if (request.getRequestData().size() < 1 + 2) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AttenderRequestPaymentTab attenderRequestPaymentTab = (AttenderRequestPaymentTab) request.getRequestData().get(1);
        return new ResponseEntity<>(attenderRequestPaymentTab.getAttenderPaymentTabList(), HttpStatus.OK);
    }


    // Returns the attendee groupName, and graders and attendee users of the same group as this user
    @GetMapping("/offeringWorkshop/{id}/groupDetails")
    public ResponseEntity<Object> showUsersOfTheSameGroupAsThisUser(@PathVariable long id, Authentication authentication) {

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

        if (attendeeGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        GroupUsersContext groupUsersContext = new GroupUsersContext();

        groupUsersContext.setGroupName(attendeeGroup.getName());
        groupUsersContext.setGroupId(attendeeGroup.getId());

        List<User> users = userRepository.findAll();

        List<User> attendeeUsers = new ArrayList<User>();

        for (WorkshopAttenderInfo attInfo : attendeeGroup.getAttenderInfos()) {
            for (User user : users) {
                Attender attender = (Attender) user.getRole("Attender");
                if (attender.getAttenderWorkshopConnection().getId() == attInfo.getWorkshopAttender().getId()) {
                    attendeeUsers.add(user);
                    break;
                }
            }
        }

        groupUsersContext.setAttendees(attendeeUsers);

        List<UserContext> graderUsers = new ArrayList<>();
        for (WorkshopGraderInfo workshopGraderInfo1 : attendeeGroup.getGraderInfos()) {
            for (User user : users) {
                Grader grader = (Grader) user.getRole("Grader");
                if (grader.getGraderWorkshopConnection().getId() == workshopGraderInfo1.getWorkshopGrader().getId()) {
                    UserContext userContext = new UserContext();
                    userContext.setUsername(user.getUsername());
                    userContext.setEmail(user.getEmail());
                    userContext.setName(user.getName());
                    userContext.setId(user.getId());
                    userContext.setStarred(workshopGraderInfo1.isStarred());
                    graderUsers.add(userContext);
                    break;
                }
            }
        }

        groupUsersContext.setGraders(graderUsers);

        return new ResponseEntity<>(groupUsersContext, HttpStatus.OK);
    }


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
