package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "checkouts")
public class Checkout extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    public ShoppingCart shoppingCart;

    @OneToOne
    @JoinColumn(name = "order_id")
    public Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    public CheckoutStatus status = CheckoutStatus.IN_PROGRESS;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "completed_at")
    public Instant completedAt;
}
