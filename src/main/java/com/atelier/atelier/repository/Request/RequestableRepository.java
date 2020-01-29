package com.atelier.atelier.repository.Request;

import com.atelier.atelier.entity.RequestService.Requestable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestableRepository extends JpaRepository<Requestable, Long> {
}
