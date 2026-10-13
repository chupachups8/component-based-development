package com.vezbe.paymentservice.repository;

import com.vezbe.paymentservice.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt,Integer> {
    int countByPayment_Id(Integer paymentId);
}
