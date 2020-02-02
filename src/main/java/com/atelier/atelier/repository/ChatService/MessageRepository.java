package com.atelier.atelier.repository.ChatService;

import com.atelier.atelier.entity.MessagingSystem.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
