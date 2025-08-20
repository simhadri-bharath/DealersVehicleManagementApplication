package com.bharath.task.dto;
import com.bharath.task.entity.Payment;
import lombok.Data;
@Data
public class PaymentRequest {
private Long dealerId;
private Double amount;
private Payment.PaymentMethod method;
}