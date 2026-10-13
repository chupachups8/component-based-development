package com.cinema.movieservice.controller;

import com.cinema.movieservice.dto.ScreeningRequest;
import com.cinema.movieservice.model.Screening;
import com.cinema.movieservice.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(
            ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    public List<Screening> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    @GetMapping("/{id}")
    public Screening getScreeningById(
            @PathVariable Integer id) {

        return screeningService.getScreeningById(id);
    }

    @GetMapping("/movie/{movieId}")
    public List<Screening> getScreeningsByMovie(
            @PathVariable Integer movieId) {

        return screeningService.getScreeningsByMovieId(movieId);
    }

    @PostMapping
    public ResponseEntity<Screening> createScreening(
            @Valid @RequestBody ScreeningRequest request) {

        Screening screening =
                screeningService.createScreening(request);

        return new ResponseEntity<>(
                screening,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public Screening updateScreening(
            @PathVariable Integer id,
            @Valid @RequestBody ScreeningRequest request) {

        return screeningService
                .updateScreening(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(
            @PathVariable Integer id) {

        screeningService.deleteScreening(id);

        return ResponseEntity.noContent().build();
    }
}
