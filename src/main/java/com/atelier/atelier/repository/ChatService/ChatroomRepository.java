package com.atelier.atelier.repository.ChatService;

import com.atelier.atelier.entity.MessagingSystem.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
