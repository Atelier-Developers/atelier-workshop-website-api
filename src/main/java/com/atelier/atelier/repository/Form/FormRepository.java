package com.atelier.atelier.repository.Form;

import com.atelier.atelier.entity.FormService.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository<T extends Form> extends JpaRepository<T, Long> {
}
