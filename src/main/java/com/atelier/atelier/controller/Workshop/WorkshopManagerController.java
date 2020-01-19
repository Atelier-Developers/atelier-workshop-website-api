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
import com.atelier.atelier.repository.Request.RequesterRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.*;
import org.aspectj.weaver.NameMangler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import javax.xml.crypto.Data;
import java.io.IOException;
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
    private FileAnswerRepository fileAnswerRepository;

    public WorkshopManagerController( FileAnswerRepository fileAnswerRepository, WorkshopGroupRepository workshopGroupRepository, RequesterRepository requesterRepository, GraderRequestFormRepository graderRequestFormRepository, AttenderRegisterFormRepository attenderRegisterFormRepository, WorkshopFormRepository workshopFormFormRepository, GraderEvaluationFormRepository graderEvaluationFormFormRepository, RequestRepository requestRepository, WorkshopRepository workshopRepository, OfferingWorkshopRepository offeringWorkshopRepository, UserRepository userRepository, FormRepository formRepository, QuestionRepsoitory questionRepsoitory, WorkshopGraderInfoRepository workshopGraderInfoRepository, AnswerRepository answerRepository, WorkshopAttenderInfoRepository workshopAttenderInfoRepository) {
        this.workshopRepository = workshopRepository;
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
        this.fileAnswerRepository = fileAnswerRepository;
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

        for (OfferedWorkshop offeredWorkshop : managerWorkshopConnection.getOfferedWorkshops()){

            OfferedWorkshopManagerNameContext offeredWorkshopManagerNameContext = new OfferedWorkshopManagerNameContext();
            offeredWorkshopManagerNameContext.setOfferedWorkshop(offeredWorkshop);
            offeredWorkshopManagerNameContext.setWorkshopManagerName(user.getName());

            offeredWorkshopManagerNameContexts.add(offeredWorkshopManagerNameContext);
        }

        return new ResponseEntity<>(offeredWorkshopManagerNameContexts, HttpStatus.OK);
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
    public ResponseEntity<Object> getOfferingWorkshop(Authentication authentication, @PathVariable Long id) {
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

    @PostMapping("/offeringWorkshop/{id}/attenderRegisterForm")
    public ResponseEntity<Object> makeAttenderRegisterForm(@PathVariable long id, Authentication authentication, @RequestBody AttenderRegisterForm attenderRegisterForm) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                attenderRegisterForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setAttenderRegisterForm(attenderRegisterForm);
                try {
                    attenderRegisterFormRepository.save(attenderRegisterForm);
                } catch (DataIntegrityViolationException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/offeringWorkshop/{id}/attenderRegisterForm")
    public ResponseEntity<Object> getAttenderRegisterForm(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (optionalOfferedWorkshop.isPresent()) {
            OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                AttenderRegisterForm attenderRegisterForm = offeredWorkshop.getAttenderRegisterForm();
                if (attenderRegisterForm == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(attenderRegisterForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @GetMapping("/offeringWorkshop/{id}/graderRequestForm")
    public ResponseEntity<Object> getGraderRequestForm(@PathVariable long id, Authentication authentication) {
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (optionalOfferedWorkshop.isPresent()) {
            OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                GraderRequestForm graderRequestForm = offeredWorkshop.getGraderRequestForm();
                if (graderRequestForm == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(graderRequestForm, HttpStatus.OK);
            }
            return new ResponseEntity<>("The offering workshop that you requested is not permitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No Offered Workshop with the id provided was found.", HttpStatus.NO_CONTENT);

    }

    @PostMapping("/offeringWorkshop/{id}/graderRequestForm")
    public ResponseEntity<Object> makeGraderRequestForm(@PathVariable long id, Authentication authentication, @RequestBody GraderRequestForm graderRequestForm) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);
        Optional<OfferedWorkshop> offeredWorkshopOptional = offeringWorkshopRepository.findById(id);
        if (offeredWorkshopOptional.isPresent()) {
            OfferedWorkshop offeredWorkshop = offeredWorkshopOptional.get();
            if (offeredWorkshop.getWorkshopManager().getId() == managerWorkshopConnection.getId()) {
                graderRequestForm.setOfferedWorkshop(offeredWorkshop);
                offeredWorkshop.setGraderRequestForm(graderRequestForm);
                try {
                    graderRequestFormRepository.save(graderRequestForm);
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
                    } else if (type.equalsIgnoreCase("FileAnswer")){
                        FileAnswer fileAnswer = new FileAnswer();
                        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                        fileAnswer.setFileName(fileName);
                        fileAnswer.setFileType(multipartFile.getContentType());
                        fileAnswer.setData(multipartFile.getBytes());

                        answerData = fileAnswer;

//                        // TODO ADDED FILE STUFF HERE
//                        fileAnswerRepository.save(fileAnswer);
                    }
                    else {
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
        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
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
        form.setQuestions(questions);

        for (Question question : questions) {
            List<Answerable> answerables = question.getAnswerables();
            if (answerables != null) {
                for (Answerable answerable : answerables) {
                    answerable.setQuestion(question);
                }
            }

            question.setForm(form);
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


    // Returns Attendee Request Object (Both Accepted and Pending and Rejected)
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



    @GetMapping("/offeringWorkshop/form/{id}/result")
    public ResponseEntity<Object> getResultOfASingleFormApplicant(@PathVariable long id, @RequestBody long requesterId) {

        Optional<Form> optionalForm = formRepository.findById(id);

        if (!optionalForm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Form form = optionalForm.get();

        Optional<Requester> optionalRequester = requesterRepository.findById(requesterId);

        if ( !optionalRequester.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Requester requester = optionalRequester.get();

        if ((form instanceof AttenderRegisterForm) && (requester instanceof Attender)){

            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for(Question question : questions){
                for (Answer answer : question.getAnswers()){
                    AttenderFormApplicant attenderFormApplicant = (AttenderFormApplicant)answer.getFormApplicant();
                    AttenderWorkshopConnection attenderWorkshopConnection = (AttenderWorkshopConnection)attenderFormApplicant.getWorkshopAttender();
                    if ( attenderWorkshopConnection.getAttender().getId() == requester.getId() ){
                        FormResultContext formResultContext = new FormResultContext();
                        formResultContext.setQuestion(question);
                        formResultContext.setAnswer(answer);
                        formResultContexts.add(formResultContext);
                    }
                }
            }
            return new ResponseEntity<>(formResultContexts, HttpStatus.OK);

        }

        else if ((form instanceof GraderRequestForm) && (requester instanceof Grader)){

            List<FormResultContext> formResultContexts = new ArrayList<>();

            List<Question> questions = form.getQuestions();
            for(Question question : questions){
                for (Answer answer : question.getAnswers()){
                    GraderFormApplicant attenderFormApplicant = (GraderFormApplicant)answer.getFormApplicant();
                    GraderWorkshopConnection attenderWorkshopConnection = (GraderWorkshopConnection) attenderFormApplicant.getWorkshopGrader();
                    if ( attenderWorkshopConnection.getGrader().getId() == requester.getId() ){
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
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/offeringWorkshop/{id}/request")
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

            if ( request.getState() == RequestState.Accepted ){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String requestStateString = requestStatusContext.getRequestState();
            RequestState requestState = null;

            if (requestStateString.equalsIgnoreCase("ACCEPTED") ){
                requestState = RequestState.Accepted;
            }
            else if (requestStateString.equalsIgnoreCase("REJECTED")){
                requestState = RequestState.Rejected;
            }

            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            request.setState(requestState);

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


    //Returns group objects of the offering workshop with the workshopInfo and Connection Info Ids
    @GetMapping("/offeringWorkshop/{id}/groups")
    public ResponseEntity<Object> getGroups(@PathVariable long id, Authentication authentication) {

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Set<WorkshopGroup> workshopGroupSet = offeredWorkshop.workshopGroupSet();

        List<GroupElementContext> groupElementContexts = new ArrayList<>();

        for(WorkshopGroup workshopGroup : workshopGroupSet){
            GroupElementContext groupElementContext = new GroupElementContext();
            groupElementContext.setGroupId(workshopGroup.getId());
            groupElementContext.setGroupName(workshopGroup.getName());
            for (WorkshopGraderInfo workshopGraderInfo : workshopGroup.getGraderInfos()){
                GroupMemberContext groupMemberContext = new GroupMemberContext();
                groupMemberContext.setWorkshopInfoId(workshopGraderInfo.getId());
                groupMemberContext.setWorkshopConnectionId(workshopGraderInfo.getWorkshopGrader().getId());
                groupElementContext.addGraderInfo(groupMemberContext);
            }
            for (WorkshopAttenderInfo workshopAttenderInfo : workshopGroup.getAttenderInfos()){
                GroupMemberContext groupMemberContext = new GroupMemberContext();
                groupMemberContext.setWorkshopInfoId(workshopAttenderInfo.getId());
                groupMemberContext.setWorkshopConnectionId(workshopAttenderInfo.getWorkshopAttender().getId());
                groupElementContext.addAttendeeInfo(groupMemberContext);
            }
            groupElementContexts.add(groupElementContext);
        }

        return new ResponseEntity<>(groupElementContexts, HttpStatus.OK);
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
            if ((groupWorkshopContext.getAttendersId() == null || groupWorkshopContext.getAttendersId().isEmpty()) && (groupWorkshopContext.getGradersId() == null || groupWorkshopContext.getGradersId().isEmpty()) ){
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

                if (workshopGraderInfo.getWorkshopGroup() != null ){
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

                if ( workshopAttenderInfo.getWorkshopGroup() != null ){
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


    // Returns Attendee Info Objects of the Offering Workshop
    @GetMapping("/offeringWorkshop/{id}/attendeeInfos")
    public ResponseEntity<Object> showAllAttendeeInfos(@PathVariable long id, Authentication authentication){
        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getAttenderInfos(), HttpStatus.OK);
    }


    // Returns Grader Info Objects
    @GetMapping("/offeringWorkshop/{id}/graderInfos")
    public ResponseEntity<Object> showAllGraderInfos(@PathVariable long id, Authentication authentication){

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);

        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        return new ResponseEntity<>(offeredWorkshop.getWorkshopGraderInfos(), HttpStatus.OK);
    }


    // Returns a list of contexts including the name of the group and the list of its grader and attendee users
    @GetMapping("/offeringWorkshop/{id}/groupDetails")
    public ResponseEntity<Object> showGroupsWithAllOfItsUsers(@PathVariable long id, Authentication authentication){

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        List<User> users = userRepository.findAll();

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Set<WorkshopGroup> workshopGroupSet = offeredWorkshop.workshopGroupSet();

        List<GroupUsersContext> groupUsersContexts = new ArrayList<GroupUsersContext>();

        for(WorkshopGroup workshopGroup : workshopGroupSet){
            GroupUsersContext groupUsersContext = new GroupUsersContext();
            groupUsersContext.setGroupName(workshopGroup.getName());
            groupUsersContext.setGroupId(workshopGroup.getId());

            List<User> graderUsers = new ArrayList<User>();
            for (WorkshopGraderInfo workshopGraderInfo : workshopGroup.getGraderInfos()){
                for (User user : users) {
                    Grader grader = (Grader) user.getRole("Grader");
                    if (grader.getGraderWorkshopConnection().getId() == workshopGraderInfo.getWorkshopGrader().getId()){
                        graderUsers.add(user);
                        break;
                    }
                }
            }

            groupUsersContext.setGraders(graderUsers);

            List<User> attendeeUsers = new ArrayList<User>();
            for (WorkshopAttenderInfo workshopAttenderInfo : workshopGroup.getAttenderInfos()){
                for (User user: users){
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
    public ResponseEntity<Object> showGraderUsersWithPendingRequests(@PathVariable long id, Authentication authentication){

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> graders = new ArrayList<User>();

        List<User> users = userRepository.findAll();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Grader) {
                if (request.getState().equals(RequestState.Pending)){
                    Grader grader = (Grader) request.getRequester();
                    WorkshopGrader workshopGrader = grader.getGraderWorkshopConnection();
                    for (User user: users){
                        Grader userGrader = (Grader) user.getRole("Grader");
                        if (userGrader.getGraderWorkshopConnection().getId() == workshopGrader.getId() ){
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
    public ResponseEntity<Object> showPendingAttendeeRequestsUsers(@PathVariable long id, Authentication authentication){

        ManagerWorkshopConnection managerWorkshopConnection = getMangerFromAuthentication(authentication);

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(id);
        if (!optionalOfferedWorkshop.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        if (offeredWorkshop.getWorkshopManager().getId() != managerWorkshopConnection.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> attendees = new ArrayList<User>();

        List<User> users = userRepository.findAll();

        for (Request request : offeredWorkshop.getRequests()) {
            if (request.getRequester() instanceof Attender) {
                if (request.getState().equals(RequestState.Pending)){
                    Attender attender = (Attender) request.getRequester();
                    WorkshopAttender workshopAttender = attender.getAttenderWorkshopConnection();
                    for (User user : users){
                        Attender userAttender = (Attender) user.getRole("Attender");
                        if (userAttender.getAttenderWorkshopConnection().getId() == workshopAttender.getId()){
                            attendees.add(user);
                            break;
                        }
                    }
                }
            }
        }

        return new ResponseEntity<>(attendees, HttpStatus.OK);

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
