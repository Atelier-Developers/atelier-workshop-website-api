package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

}
