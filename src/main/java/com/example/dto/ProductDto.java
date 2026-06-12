package com.example.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String sku,
        Integer stockQuantity,
        Instant createdAt
) {
}
