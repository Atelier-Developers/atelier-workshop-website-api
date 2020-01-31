package com.atelier.atelier.repository.Request;

import com.atelier.atelier.entity.RequestService.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
