package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopManagerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopManagerInfoRepository extends JpaRepository<WorkshopManagerInfo, Long> {
}
