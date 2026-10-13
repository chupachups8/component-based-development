package com.cinema.reservaitionservice.controller;

import com.cinema.reservaitionservice.client.MovieClient;
import com.cinema.reservaitionservice.dto.ScreeningResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class FeignTestController {
    private MovieClient movieClient;
    public FeignTestController(MovieClient movieClient) {
        this.movieClient = movieClient;
    }
    @GetMapping("/screening/{id}")
    public ScreeningResponse getScreening(@PathVariable Integer id) {
        return movieClient
                .getScreening(id);
    }
}
