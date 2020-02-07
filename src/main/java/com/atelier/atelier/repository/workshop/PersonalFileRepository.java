package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.PersonalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalFileRepository extends JpaRepository<PersonalFile, Long> {
}
