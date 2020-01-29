package com.atelier.atelier.controller.Workshop;

import com.atelier.atelier.context.WorkshopFormContext;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.WorkshopForm;
import com.atelier.atelier.repository.Form.FormRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/workshopForms")
public class WorkshopFormController {

    private FormRepository formRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;


    public WorkshopFormController(FormRepository formRepository, OfferingWorkshopRepository offeringWorkshopRepository) {
        this.formRepository = formRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
    }


    @PostMapping("/form")
    public ResponseEntity<Object> createForm(@RequestBody WorkshopFormContext workshopFormContext) {
        WorkshopForm workshopForm = workshopFormContext.getWorkshopForm();
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(workshopFormContext.getOfferedWorkshopId());
        if ( !optionalOfferedWorkshop.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();
        workshopForm.setOfferedWorkshop(offeredWorkshop);
        offeredWorkshop.addWorkshopForm(workshopForm);
        formRepository.save(workshopForm);
        return new ResponseEntity<>(workshopForm, HttpStatus.CREATED);
    }

}
