package com.atelier.atelier.repository.Request;

import com.atelier.atelier.entity.RequestService.Requester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequesterRepository extends JpaRepository<Requester, Long> {
}
