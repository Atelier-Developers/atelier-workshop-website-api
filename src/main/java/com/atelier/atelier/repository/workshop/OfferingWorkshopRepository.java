package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingWorkshopRepository extends JpaRepository<OfferedWorkshop, Long> {
}
