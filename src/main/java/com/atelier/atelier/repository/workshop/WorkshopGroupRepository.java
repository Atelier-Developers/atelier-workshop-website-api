package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.WorkshopGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopGroupRepository extends JpaRepository<WorkshopGroup, Long> {
}
