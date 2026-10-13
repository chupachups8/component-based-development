package com.cinema.reservaitionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriumResponse {
    private Integer id;
    private String name;
    private Integer capacity;
}
