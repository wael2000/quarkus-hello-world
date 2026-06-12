package com.example.dto;

import java.time.Instant;

public record ShoppingCartDto(
        Long id,
        String customerId,
        Instant createdAt,
        Instant updatedAt
) {
}
