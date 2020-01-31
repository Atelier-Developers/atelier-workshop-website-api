package com.atelier.atelier.repository.Form;

import com.atelier.atelier.entity.FormService.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
