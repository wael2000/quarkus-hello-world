package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipping")
public class Shipping extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    public Order order;

    @Column(name = "recipient_name", nullable = false)
    public String recipientName;

    @Column(name = "address_line1", nullable = false)
    public String addressLine1;

    @Column(name = "address_line2")
    public String addressLine2;

    @Column(nullable = false, length = 128)
    public String city;

    @Column(name = "postal_code", nullable = false, length = 32)
    public String postalCode;

    @Column(nullable = false, length = 2)
    public String country;

    @Column(length = 128)
    public String carrier;

    @Column(name = "tracking_number", length = 128)
    public String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    public ShippingStatus status = ShippingStatus.PENDING;
}
