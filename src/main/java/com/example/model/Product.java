package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    public String name;

    public String description;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal price;

    @Column(nullable = false, unique = true, length = 64)
    public String sku;

    @Column(name = "stock_quantity", nullable = false)
    public Integer stockQuantity = 0;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();
}
