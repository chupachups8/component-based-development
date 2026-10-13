package com.vezbe.paymentservice.repository;

import com.vezbe.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Optional<Payment> findByReservationId(Integer reservationId);
}
