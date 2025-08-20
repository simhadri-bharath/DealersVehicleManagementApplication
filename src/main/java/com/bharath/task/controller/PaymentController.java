package com.bharath.task.controller;

import com.bharath.task.dto.PaymentDto;
import com.bharath.task.dto.PaymentRequest;
import com.bharath.task.entity.Payment;
import com.bharath.task.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <-- IMPORT THIS for PathVariable & GetMapping

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        Payment initiatedPayment = paymentService.initiatePayment(paymentRequest);
        return ResponseEntity.ok(initiatedPayment);
    }
    
    // --- REPLACE THE OLD getPaymentStatus METHOD WITH THIS ONE ---
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentStatus(@PathVariable Long paymentId) {
        return paymentService.getPaymentDtoById(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}