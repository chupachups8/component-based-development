package com.cinema.reservaitionservice.client;

import com.cinema.reservaitionservice.dto.ScreeningResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("movie-service")
public interface MovieClient {
    @GetMapping("/screenings/{id}")
    ScreeningResponse getScreening(@PathVariable Integer id);
}
