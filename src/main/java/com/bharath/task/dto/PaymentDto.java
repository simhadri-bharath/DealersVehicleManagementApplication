package com.bharath.task.dto;

import com.bharath.task.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Double amount;
    private Payment.PaymentMethod method;
    private Payment.PaymentStatus status;
    private DealerDto dealer; // We use the DealerDto here
}