package com.atelier.atelier.repository.ChatService;

import com.atelier.atelier.entity.MessagingSystem.ChatterMessageRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatterMessageRelationRepository extends JpaRepository<ChatterMessageRelation, Long> {
}
