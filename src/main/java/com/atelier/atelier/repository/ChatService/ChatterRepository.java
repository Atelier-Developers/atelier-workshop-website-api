package com.atelier.atelier.repository.ChatService;

import com.atelier.atelier.entity.MessagingSystem.Chatter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatterRepository extends JpaRepository<Chatter, Long> {
}
