package com.vezbe.paymentservice.controller;

import com.vezbe.paymentservice.dto.PaymentRequest;
import com.vezbe.paymentservice.dto.PaymentResponse;
import com.vezbe.paymentservice.dto.RefundRequest;
import com.vezbe.paymentservice.dto.RefundResponse;
import com.vezbe.paymentservice.model.Payment;
import com.vezbe.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse payment =
                paymentService.createPayment(request);

        return new ResponseEntity<>(
                payment,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(
            @PathVariable Integer id) {

        return paymentService.getPaymentById(id);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<RefundResponse> refundPayment(
            @PathVariable Integer id,
            @Valid @RequestBody RefundRequest request) {

        RefundResponse refund =
                paymentService.refundPayment(id, request);

        return new ResponseEntity<>(
                refund,
                HttpStatus.CREATED
        );
    }
}
