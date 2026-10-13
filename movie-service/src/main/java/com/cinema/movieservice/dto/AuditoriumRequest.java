package com.cinema.movieservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriumRequest {

    @NotBlank(message = "Auditorium name is required")
    private String name;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private int capacity;
}
