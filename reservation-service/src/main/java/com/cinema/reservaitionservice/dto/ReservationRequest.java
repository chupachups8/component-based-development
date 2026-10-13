package com.cinema.reservaitionservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    @NotNull(message = "Customer id is required")
    private Integer customerId;

    @NotNull(message = "Screening id is required")
    private Integer screeningId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> seats;

}
