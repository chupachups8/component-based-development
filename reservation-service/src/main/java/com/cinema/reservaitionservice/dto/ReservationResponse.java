package com.cinema.reservaitionservice.dto;


import com.cinema.reservaitionservice.model.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Integer id;

    private Integer customerId;

    private Integer screeningId;

    private List<String> seats;

    private BigDecimal totalPrice;

    private ReservationStatus status;

    private LocalDateTime createdAt;
}
