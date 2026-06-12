package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "line_items")
public class LineItem extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @Column(nullable = false)
    public Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    public BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    public ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order order;
}
