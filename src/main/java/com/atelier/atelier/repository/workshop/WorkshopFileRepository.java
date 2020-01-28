package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopFileRepository extends JpaRepository<WorkshopFile, Long> {
}
