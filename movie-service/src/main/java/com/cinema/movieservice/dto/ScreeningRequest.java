package com.cinema.movieservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ScreeningRequest {

    @NotNull(message = "Movie id is required")
    private Integer movieId;

    @NotNull(message = "Auditorium id is required")
    private Integer auditoriumId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be greater than 0")
    private BigDecimal ticketPrice;

    private Boolean active = true;

}
