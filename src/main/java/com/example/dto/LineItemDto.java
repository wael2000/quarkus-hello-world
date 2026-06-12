package com.example.dto;

import java.math.BigDecimal;

public record LineItemDto(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice,
        Long shoppingCartId,
        Long orderId
) {
}
