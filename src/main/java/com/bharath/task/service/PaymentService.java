package com.bharath.task.service;

import com.bharath.task.dto.PaymentRequest;
import com.bharath.task.entity.Dealer;
import com.bharath.task.entity.Payment;
import com.bharath.task.repository.DealerRepository;
import com.bharath.task.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bharath.task.dto.DealerDto; // <-- IMPORT
import com.bharath.task.dto.PaymentDto; // <-- IMPORT
import java.util.Optional; // <-- IMPORT

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DealerRepository dealerRepository;
    private final PaymentService self; // Self-injection to enable async call

    // Use @Lazy to break the circular dependency during bean creation
    public PaymentService(PaymentRepository paymentRepository, DealerRepository dealerRepository, @Lazy PaymentService self) {
        this.paymentRepository = paymentRepository;
        this.dealerRepository = dealerRepository;
        this.self = self;
    }

    @Transactional
    public Payment initiatePayment(PaymentRequest request) {
        Dealer dealer = dealerRepository.findById(request.getDealerId())
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found with id: " + request.getDealerId()));

        Payment payment = Payment.builder()
                .dealer(dealer)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(Payment.PaymentStatus.PENDING)
                .build();

        // Save the initial PENDING status
        Payment savedPayment = paymentRepository.save(payment);

        // Trigger the asynchronous status update by calling it through the proxy
        self.processPaymentCallback(savedPayment.getId());

        // Return the payment with PENDING status immediately
        return savedPayment;
    }

    @Transactional(readOnly = true) // Ensures the session is open, optimized for reading
    public Optional<PaymentDto> getPaymentDtoById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(this::convertToDto); // Convert the Payment entity to a PaymentDto
    }

    // --- ADD THIS HELPER METHOD ---
    private PaymentDto convertToDto(Payment payment) {
        Dealer dealer = payment.getDealer();
        DealerDto dealerDto = new DealerDto(
                dealer.getId(),
                dealer.getName(),
                dealer.getEmail()
        );

        return new PaymentDto(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                dealerDto
        );
    }
    @Async // Marks this method to be executed in a separate thread
    @Transactional
    public void processPaymentCallback(Long paymentId) {
        try {
            // 1. Simulate the 5-second delay for the payment gateway callback
            log.info("Payment processing for paymentId: {}. Waiting for 5 seconds in background...", paymentId);
            TimeUnit.SECONDS.sleep(5);

            // 2. Retrieve and update the payment in a new transaction
            paymentRepository.findById(paymentId).ifPresent(payment -> {
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                log.info("Payment status updated to SUCCESS for paymentId: {}", paymentId);
            });

        } catch (InterruptedException e) {
            log.error("Payment processing was interrupted for paymentId: {}", paymentId, e);
            Thread.currentThread().interrupt();
        }
    }
}