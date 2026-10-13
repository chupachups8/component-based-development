package com.vezbe.paymentservice.repository;

import com.vezbe.paymentservice.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Integer> {
    boolean existsByPayment_Id(Integer paymentId);
}
