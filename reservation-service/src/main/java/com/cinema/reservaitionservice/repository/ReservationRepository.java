package com.cinema.reservaitionservice.repository;

import com.cinema.reservaitionservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByCustomerId(Integer customerId);

    List<Reservation> findByScreeningId(Integer screeningId);
}
