package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopAttenderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopAttenderInfoRepository extends JpaRepository<WorkshopAttenderInfo, Long> {
}
