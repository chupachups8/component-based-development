package com.vezbe.paymentservice.service;

import com.vezbe.paymentservice.client.ReservationClient;
import com.vezbe.paymentservice.dto.*;
import com.vezbe.paymentservice.exception.InvalidPaymentStateException;
import com.vezbe.paymentservice.exception.ResourceAlreadyExistsException;
import com.vezbe.paymentservice.exception.ResourceNotFoundException;
import com.vezbe.paymentservice.model.*;
import com.vezbe.paymentservice.repository.PaymentAttemptRepository;
import com.vezbe.paymentservice.repository.PaymentRepository;
import com.vezbe.paymentservice.repository.RefundRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final RefundRepository refundRepository;
    private final ReservationClient reservationClient;

    public PaymentResponse createPayment(PaymentRequest request) {
        if (paymentRepository
                .findByReservationId(request.getReservationId())
                .isPresent()) {

            throw new ResourceAlreadyExistsException(
                    "Payment for reservation "
                            + request.getReservationId()
                            + " already exists"
            );
        }

        ReservationResponse reservation;

        try {
            reservation =
                    reservationClient.getReservationById(
                            request.getReservationId()
                    );
        } catch (FeignException.NotFound ex) {

            throw new ResourceNotFoundException(
                    "Reservation with id "
                            + request.getReservationId()
                            + " not found"
            );
        }

        if (!"PENDING".equals(reservation.getStatus())) {

            throw new InvalidPaymentStateException(
                    "Only PENDING reservation can be paid"
            );
        }

        Payment payment = new Payment();

        payment.setReservationId(reservation.getId());
        payment.setAmount(reservation.getTotalPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        PaymentAttempt attempt = new PaymentAttempt();

        attempt.setPayment(payment);
        attempt.setAttemptNumber(1);
        attempt.setCreatedAt(LocalDateTime.now());

        if (Boolean.TRUE.equals(request.getSuccessful())) {

            attempt.setStatus(
                    PaymentAttemptStatus.SUCCESS
            );

            paymentAttemptRepository.save(attempt);

            payment.setStatus(PaymentStatus.SUCCESS);

            payment = paymentRepository.save(payment);

            // Feign -> reservation-service
            reservationClient.confirmReservation(
                    reservation.getId()
            );

        } else {

            attempt.setStatus(
                    PaymentAttemptStatus.FAILED
            );

            paymentAttemptRepository.save(attempt);

            payment.setStatus(PaymentStatus.FAILED);

            payment = paymentRepository.save(payment);
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getReservationId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );

    }

    public Payment getPaymentById(Integer id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment with id "
                                        + id
                                        + " not found"
                        )
                );
    }
    @Transactional
    public RefundResponse refundPayment(
            Integer paymentId,
            RefundRequest request) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment with id "
                                        + paymentId
                                        + " not found"
                        )
                );

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidPaymentStateException(
                    "Only SUCCESS payment can be refunded"
            );
        }

        if (refundRepository.existsByPayment_Id(paymentId)) {
            throw new ResourceAlreadyExistsException(
                    "Refund for payment "
                            + paymentId
                            + " already exists"
            );
        }

        Refund refund = new Refund();

        refund.setPayment(payment);
        refund.setAmount(payment.getAmount());
        refund.setReason(request.getReason());
        refund.setCreatedAt(LocalDateTime.now());

        refund = refundRepository.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);

        paymentRepository.save(payment);

        reservationClient.cancelReservation(
                payment.getReservationId()
        );

        return new RefundResponse(
                refund.getId(),
                payment.getId(),
                refund.getAmount(),
                refund.getReason(),
                refund.getCreatedAt()
        );

    }
}
