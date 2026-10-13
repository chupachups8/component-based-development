package com.cinema.reservaitionservice.service;


import com.cinema.reservaitionservice.client.MovieClient;
import com.cinema.reservaitionservice.dto.ReservationRequest;
import com.cinema.reservaitionservice.dto.ReservationResponse;
import com.cinema.reservaitionservice.dto.ScreeningResponse;
import com.cinema.reservaitionservice.exception.ResourceNotFoundException;
import com.cinema.reservaitionservice.exception.SeatAlreadyReservedException;
import com.cinema.reservaitionservice.model.Customer;
import com.cinema.reservaitionservice.model.Reservation;
import com.cinema.reservaitionservice.model.ReservationStatus;
import com.cinema.reservaitionservice.model.ReservedSeat;
import com.cinema.reservaitionservice.repository.CustomerRepository;
import com.cinema.reservaitionservice.repository.ReservationRepository;
import com.cinema.reservaitionservice.repository.ReservedSeatRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    CustomerRepository customerRepository;
    ReservationRepository reservationRepository;
    ReservedSeatRepository reservedSeatRepository;
    MovieClient movieClient;

    public ReservationService(CustomerRepository customerRepository,ReservationRepository reservationRepository, ReservedSeatRepository reservedSeatRepository, MovieClient movieClient) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.reservedSeatRepository = reservedSeatRepository;
        this.movieClient = movieClient;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        Customer customer=customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Customer with id:"+request.getCustomerId()+" not found"));
        ScreeningResponse screening;
        try{
            screening=movieClient.getScreening(request.getScreeningId());
        }catch (FeignException.NotFound ex){
            throw new ResourceNotFoundException("Screening with id:"+request.getScreeningId()+" not found");
        }
        if(screening.getActive()!=true){
            throw new IllegalArgumentException("Screening is not active");
        }
        if(screening.getStartTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Screening start time is before screening end time");
        }
        Set<String> uniqueSeats=new HashSet<>(request.getSeats());
        if(uniqueSeats.size()!=request.getSeats().size()){
            throw new IllegalArgumentException("Duplicate seats are not allowed");
        }

        for(String seat: request.getSeats()){
            boolean reserved =
                    reservedSeatRepository
                            .existsByReservation_ScreeningIdAndSeatNumberAndReservation_StatusNot(
                                    request.getScreeningId(),
                                    seat,
                                    ReservationStatus.CANCELLED
                            );

            if (reserved) {
                throw new SeatAlreadyReservedException(
                        "Seat " + seat + " is already reserved"
                );
            }
        }
        BigDecimal totalPrice=
                screening.getTicketPrice()
                        .multiply(
                            BigDecimal.valueOf(request.getSeats().size())
                        );
        Reservation reservation=new Reservation();
        reservation.setCustomer(customer);
        reservation.setScreeningId(request.getScreeningId());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(ReservationStatus.PENDING);

        reservation=reservationRepository.save(reservation);

        for(String seatNumber : request.getSeats()){
            ReservedSeat reservedSeat=new ReservedSeat();
            reservedSeat.setSeatNumber(seatNumber);
            reservedSeat.setReservation(reservation);
            reservedSeat.setPrice(screening.getTicketPrice());
            reservedSeatRepository.save(reservedSeat);
        }

        return new ReservationResponse(
                reservation.getId(),
                customer.getId(),
                reservation.getScreeningId(),
                request.getSeats(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }

    public Reservation getReservationById(Integer reservationId){
        return reservationRepository
                .findById(reservationId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Reservation with id:"+reservationId+" not found"
                        )
                );
    }

    public Reservation confirmReservation(Integer id) {

        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only PENDING reservation can be confirmed"
            );
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Integer id) {

        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Reservation is already cancelled"
            );
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    public List<String> getReservedSeats(Integer screeningId) {

        return reservedSeatRepository
                .findByReservation_ScreeningIdAndReservation_StatusNot(
                        screeningId,
                        ReservationStatus.CANCELLED
                )
                .stream()
                .map(ReservedSeat::getSeatNumber)
                .toList();
    }
}
