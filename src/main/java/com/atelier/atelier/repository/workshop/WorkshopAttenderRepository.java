package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopAttenderRepository extends JpaRepository<WorkshopAttender, Long> {
}
