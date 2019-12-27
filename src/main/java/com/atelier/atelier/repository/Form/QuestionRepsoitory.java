package com.atelier.atelier.repository.Form;

import com.atelier.atelier.entity.FormService.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepsoitory extends JpaRepository<Question, Long> {
}
