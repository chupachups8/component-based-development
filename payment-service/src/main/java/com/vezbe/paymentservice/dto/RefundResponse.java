package com.vezbe.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {
    private Integer id;
    private Integer paymentId;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime createdAt;
}
