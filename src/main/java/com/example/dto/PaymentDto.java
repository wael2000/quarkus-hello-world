package com.example.dto;

import com.example.model.PaymentMethod;
import com.example.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentDto(
        Long id,
        Long orderId,
        Long checkoutId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        String transactionId,
        Instant paidAt
) {
}
