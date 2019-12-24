package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGraderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopGraderInfoRepository extends JpaRepository<WorkshopGraderInfo, Long> {
}
