package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    public Order order;

    @OneToOne(optional = false)
    @JoinColumn(name = "checkout_id", nullable = false, unique = true)
    public Checkout checkout;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    public PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    public PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "transaction_id", length = 128)
    public String transactionId;

    @Column(name = "paid_at")
    public Instant paidAt;
}
