package com.example.dto;

import com.example.model.ShippingStatus;

public record ShippingDto(
        Long id,
        Long orderId,
        String recipientName,
        String addressLine1,
        String addressLine2,
        String city,
        String postalCode,
        String country,
        String carrier,
        String trackingNumber,
        ShippingStatus status
) {
}
