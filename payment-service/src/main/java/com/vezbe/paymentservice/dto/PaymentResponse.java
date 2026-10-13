package com.vezbe.paymentservice.dto;

import com.vezbe.paymentservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Integer id;

    private Integer reservationId;

    private BigDecimal amount;

    private PaymentStatus status;

    private LocalDateTime createdAt;
}
