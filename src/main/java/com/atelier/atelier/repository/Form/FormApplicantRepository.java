package com.atelier.atelier.repository.Form;

import com.atelier.atelier.entity.FormService.FormApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormApplicantRepository extends JpaRepository<FormApplicant, Long> {
}
