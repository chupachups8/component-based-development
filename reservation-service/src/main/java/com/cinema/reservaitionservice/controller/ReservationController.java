package com.cinema.reservaitionservice.controller;

import com.cinema.reservaitionservice.dto.ReservationRequest;
import com.cinema.reservaitionservice.dto.ReservationResponse;
import com.cinema.reservaitionservice.model.Reservation;
import com.cinema.reservaitionservice.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request) {

        ReservationResponse response =
                reservationService.createReservation(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(
            @PathVariable Integer id) {

        return reservationService.getReservationById(id);
    }

    @PutMapping("/{id}/confirm")
    public Reservation confirmReservation(
            @PathVariable Integer id) {

        return reservationService.confirmReservation(id);
    }

    @PutMapping("/{id}/cancel")
    public Reservation cancelReservation(
            @PathVariable Integer id) {

        return reservationService.cancelReservation(id);
    }

    @GetMapping("/screenings/{screeningId}/reserved-seats")
    public List<String> getReservedSeats(
            @PathVariable Integer screeningId
    ) {
        return reservationService.getReservedSeats(screeningId);
    }
}
