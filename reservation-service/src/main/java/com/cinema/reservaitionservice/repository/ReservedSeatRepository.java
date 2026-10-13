package com.cinema.reservaitionservice.repository;

import com.cinema.reservaitionservice.model.Reservation;
import com.cinema.reservaitionservice.model.ReservationStatus;
import com.cinema.reservaitionservice.model.ReservedSeat;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat,Integer> {
    boolean existsByReservation_ScreeningIdAndSeatNumberAndReservation_StatusNot(
            Integer screeningId, String seatNumber, ReservationStatus reservationStatus
    );
    List<ReservedSeat> findByReservation_ScreeningIdAndReservation_StatusNot(
            Integer screeningId,
            ReservationStatus status
    );
}

