package com.cinema.reservaitionservice.dto;

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
public class ScreeningResponse {
    private Integer id;

    private MovieResponse movie;

    private AuditoriumResponse auditorium;

    private LocalDateTime startTime;

    private BigDecimal ticketPrice;

    private Boolean active;
}
