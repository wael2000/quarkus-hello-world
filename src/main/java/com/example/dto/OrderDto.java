package com.example.dto;

import com.example.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderDto(
        Long id,
        String orderNumber,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt
) {
}
