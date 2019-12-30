package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.FormService.*;
import com.atelier.atelier.entity.RequestService.Request;
import com.atelier.atelier.entity.RequestService.RequestState;
import com.atelier.atelier.entity.RequestService.Requester;
import com.atelier.atelier.entity.UserPortalManagment.*;
import com.atelier.atelier.entity.WorkshopManagment.*;
import com.atelier.atelier.repository.Form.*;
import com.atelier.atelier.repository.Request.RequestRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    public WorkshopManagerController(WorkshopFormRepository workshopFormFormRepository, GraderEvaluationFormRepository graderEvaluationFormFormRepository, RequestRepository requestRepository, WorkshopRepository workshopRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory, WorkshopGraderInfoRepository workshopGraderInfoRepository, AnswerRepository answerRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository) {
        this.workshopRepository = workshopRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.userRepository = userRepository;
        this.questionRepsoitory = questionRepsoitory;
        this.graderEvaluationFormFormRepository = graderEvaluationFormFormRepository;
        this.workshopFormFormRepository = workshopFormFormRepository;
        this.workshopGraderInfoRepository = workshopGraderInfoRepository;
        this.answerRepository = answerRepository;
        this.requestRepository = requestRepository;
        this.workshopAttenderInfoRepository = workshopAttenderInfoRepository;
        this.workshopGroupRepository = workshopGroupRepository;
    }

    @GetMapping("/offeringWorkshop")
    public ResponseEntity<Object> showAllOfferedWorkshop(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        Role role = user.getRole("ManagerWorkshopConnection");
        if (role == null) {
            return new ResponseEntity<>("User has no workshop manager role", HttpStatus.NO_CONTENT);
        }
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) role;
        return new ResponseEntity<>(managerWorkshopConnection.getOfferedWorkshops(), HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop")
    public ResponseEntity<Object> addOfferingWorkshop(@RequestBody OfferingWorkshopContext offeringWorkshopContext, Authentication authentication) throws ParseException {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
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
        offeredWorkshop.setWorkshopManager(managerWorkshopConnection);
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

        offeringWorkshopRepository.save(offeredWorkshop);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/offeringWorkshop/{id}")
    public ResponseEntity<Object> showAllOfferedWorkshopForms(Authentication authentication, @PathVariable Long id) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
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
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
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
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                System.out.println("kir");
                List<WorkshopForm> workshopForm = offeredWorkshop.getWorkshopForms();
                if(workshopForm.isEmpty()){
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
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                workshopForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.addWorkshopForm(workshopForm);
                try {
                    workshopFormFormRepository.save(workshopForm);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
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
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                graderEvaluationForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setGraderEvaluationForm(graderEvaluationForm);
                try {
                    graderEvaluationFormFormRepository.save(graderEvaluationForm);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
    }


    @PostMapping("/offeringWorkshop/{id}/graderEvaluationForm/answer")
    public ResponseEntity<Object> answerToGraderEvalForm(@PathVariable long id, @RequestBody FormAnswerContext formAnswerContext, Authentication authentication) {
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
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {

                WorkshopGraderFormApplicant workshopGraderFormApplicant = new WorkshopGraderFormApplicant();
                WorkshopManagerFormFiller workshopManagerFormFiller = new WorkshopManagerFormFiller();
                workshopManagerFormFiller.setWorkshopManager(managerWorkshopConnection);
                managerWorkshopConnection.addFormFiller(workshopManagerFormFiller);
                workshopGraderFormApplicant.setWorkshopGraderInfo(workshopGraderInfo);
                workshopGraderInfo.addWorkshopGraderFormApplicants(workshopGraderFormApplicant);

                List<Answer> answers = new ArrayList<>();
                for (AnswerQuestionContext answerQuestionContext : formAnswerContext.getAnswerQuestionContexts()) {

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
                        System.out.println("here");
                    } else if (type.equalsIgnoreCase("ChoiceAnswer")) {
                        ChoiceAnswer choiceAnswer = new ChoiceAnswer();
                        choiceAnswer.setChoice((Integer) answerDataObject.get("choice"));
                        answerData = choiceAnswer;
                    } else {
                        return new ResponseEntity<>("Type not supported", HttpStatus.BAD_REQUEST);
                    }
                    //TODO fix file answer

//                    else if (type.equalsIgnoreCase("FileAnswer")){
//                        FileAnswer fileAnswer = new FileAnswer();
//                        fileAnswer.
//                    }

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
//
//    @PostMapping("/offeringWorkshop/{id}/form/questions")
//    public ResponseEntity<Object> addQuestion(@RequestBody FormQuestionContext formQuestionContext, @PathVariable long id, Authentication authentication) {
//
//        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
//        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
//        if (!offeredWorkshopOptional.isPresent()) {
//            return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);
//        }
//        OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
//        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        Optional<Form> optionalForm = formRepository.findById(formQuestionContext.getFormId());
//        if (!optionalForm.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        Form form = optionalForm.get();
//
//        if (form instanceof GraderEvaluationForm) {
//            GraderEvaluationForm graderEvaluationForm = (GraderEvaluationForm) form;
//            if (graderEvaluationForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        } else if (form instanceof WorkshopForm) {
//            WorkshopForm workshopForm = (WorkshopForm) form;
//            if (workshopForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        } else if (form instanceof AttenderRegisterForm) {
//            AttenderRegisterForm attenderRegisterForm = (AttenderRegisterForm) form;
//            if (attenderRegisterForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        } else if (form instanceof GraderRequestForm) {
//            GraderRequestForm graderRequestForm = (GraderRequestForm) form;
//            if (graderRequestForm.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        List<Question> questions = formQuestionContext.getQuestion();
//        form.setQuestions(questions);
//
//        for (Question question : questions) {
//            List<Answerable> answerables = question.getAnswerables();
//            if (answerables != null) {
//                for (Answerable answerable : answerables) {
//                    answerable.setQuestion(question);
//                }
//            }
//
//            question.setForm(form);
//            questionRepsoitory.save(question);
//        }
//        formRepository.save(form);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }


    private ManagerWorkshopConnection getMangerFromAuthentication(Authentication authentication) {
        User user = User.getUser(authentication, userRepository);
        ManagerWorkshopConnection managerWorkshopConnection = (ManagerWorkshopConnection) user.getRole("ManagerWorkshopConnection");
        return managerWorkshopConnection;
    }


    @GetMapping("/offeringWorkshop/{id}/requests/graders")
    public ResponseEntity<Object> getGraderRequests(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
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

    @GetMapping("/offeringWorkshop/{id}/requests/attendee")
    public ResponseEntity<Object> getAttendeeRequests(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
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


    @PostMapping("/offeringWorkshop/request/{id}")
    public ResponseEntity<Object> setRequestStatus(@PathVariable long id, Authentication authentication, @RequestBody List<RequestStatusContext> requestStatusContexts) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        List<Request> requests = new ArrayList<>();
        for (RequestStatusContext requestStatusContext : requestStatusContexts) {

            Optional<Request> optionalRequest = requestRepository.findById(requestStatusContext.getRequestId());

            if (!optionalRequest.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Request request = optionalRequest.get();

            request.setState(requestStatusContext.getRequestState());

            requests.add(request);

        }
        for (Request request : requests) {
            if (request.getState() == RequestState.Accepted) {
                Requester requester = request.getRequester();
                if (requester instanceof Attender) {
                    Attender attender = (Attender) requester;
                    enrollAttendeeWorkshop(attender.getAttenderWorkshopConnection(), offeredWorkshop);
                } else if (requester instanceof Grader) {
                    Grader grader = (Grader) requester;
                    enrollGraderWorkshop(grader.getGraderWorkshopConnection(), offeredWorkshop);
                }
            }
            requestRepository.save(request);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @GetMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> getGroups(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        //TODO WARNING FOR SET
        return new ResponseEntity<>(offeredWorkshop.workshopGroupSet(), HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> addGroups(@PathVariable long id, Authentication authentication, @RequestBody List<GroupWorkshopContext> groupWorkshopContexts) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        List<WorkshopGroup> workshopGroups = new ArrayList<>();
        for (GroupWorkshopContext groupWorkshopContext : groupWorkshopContexts) {
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

                workshopGroup.addGrader(workshopGraderInfo);
                workshopGraderInfo.setWorkshopGroup(workshopGroup);
            }

            for (Long attenderId : groupWorkshopContext.getAttenderId()) {
                Optional<WorkshopAttenderInfo> optionalWorkshopAttenderInfo = workshopAttenderInfoRepository.findById(attenderId);
                if (!optionalWorkshopAttenderInfo.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                WorkshopAttenderInfo workshopAttenderInfo = optionalWorkshopAttenderInfo.get();
                if (workshopAttenderInfo.getOfferedWorkshop().getId() != offeredWorkshop.getId()) {
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


    private void enrollGraderWorkshop(WorkshopGrader workshopGrader, OfferedWorkshop offeredWorkshop) {
        WorkshopGraderInfo workshopGraderInfo = new WorkshopGraderInfo();
        workshopGraderInfo.setWorkshopGrader(workshopGrader);
        workshopGraderInfo.setOfferedWorkshop(offeredWorkshop);
        offeredWorkshop.addWorkshopGraderrInfo(workshopGraderInfo);
        workshopGrader.addGraderInfo(workshopGraderInfo);
        workshopGraderInfoRepository.save(workshopGraderInfo);
    }

    private void enrollAttendeeWorkshop(WorkshopAttender workshopAttender, OfferedWorkshop offeredWorkshop) {
        WorkshopAttenderInfo workshopAttenderInfo = new WorkshopAttenderInfo();
        workshopAttenderInfo.setOfferedWorkshop(offeredWorkshop);
        workshopAttenderInfo.setWorkshopAttender(workshopAttender);
        workshopAttender.addWorkshopAttenderInfo(workshopAttenderInfo);
        offeredWorkshop.addWorkshopAttenderInfo(workshopAttenderInfo);
        workshopAttenderInfoRepository.save(workshopAttenderInfo);
    }
}
