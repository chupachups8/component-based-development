package com.cinema.movieservice.controller;


import com.cinema.movieservice.dto.AuditoriumRequest;
import com.cinema.movieservice.model.Auditorium;
import com.cinema.movieservice.service.AuditoriumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoriums")
public class AuditoriumController {
    AuditoriumService auditoriumService;
    public AuditoriumController(
            AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping
    public List<Auditorium> getAllAuditoriums() {
        return auditoriumService.getAllAuditoriums();
    }

    @GetMapping("/{id}")
    public Auditorium getAuditoriumById(
            @PathVariable Integer id) {

        return auditoriumService.getAuditoriumById(id);
    }
    @PostMapping
    public ResponseEntity<Auditorium> createAuditorium(
            @Valid @RequestBody AuditoriumRequest request) {

        Auditorium auditorium =
                auditoriumService.createAuditorium(request);

        return new ResponseEntity<>(
                auditorium,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public Auditorium updateAuditorium(
            @PathVariable Integer id,
            @Valid @RequestBody AuditoriumRequest request) {

        return auditoriumService
                .updateAuditorium(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditorium(
            @PathVariable Integer id) {

        auditoriumService.deleteAuditorium(id);

        return ResponseEntity.noContent().build();
    }

}
