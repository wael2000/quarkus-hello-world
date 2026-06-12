package com.example.dto;

import com.example.model.CheckoutStatus;

import java.time.Instant;

public record CheckoutDto(
        Long id,
        Long shoppingCartId,
        Long orderId,
        CheckoutStatus status,
        Instant createdAt,
        Instant completedAt
) {
}
