package com.atelier.atelier.repository.Payment;

import com.atelier.atelier.entity.PaymentService.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
