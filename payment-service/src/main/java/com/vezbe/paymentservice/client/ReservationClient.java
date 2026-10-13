package com.vezbe.paymentservice.client;

import com.vezbe.paymentservice.dto.ReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="reservation-service")
public interface ReservationClient {
    @GetMapping("/reservations/{id}")
    ReservationResponse getReservationById(
            @PathVariable("id") Integer id
    );

    @PutMapping("/reservations/{id}/confirm")
    void confirmReservation(
            @PathVariable("id") Integer id
    );

    @PutMapping("/reservations/{id}/cancel")
    void cancelReservation(
            @PathVariable("id") Integer id
    );
}
