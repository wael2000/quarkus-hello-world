package com.example.service;

import com.example.model.Checkout;
import com.example.model.LineItem;
import com.example.model.Order;
import com.example.model.Payment;
import com.example.model.Product;
import com.example.model.Shipping;
import com.example.model.ShoppingCart;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class TestDataSeederTest {

    @Test
    void seedsAllModelEntities() {
        assertEquals(4, Product.count());
        assertEquals(2, ShoppingCart.count());
        assertEquals(8, LineItem.count());
        assertEquals(2, Order.count());
        assertEquals(2, Checkout.count());
        assertEquals(1, Shipping.count());
        assertEquals(1, Payment.count());

        assertTrue(Product.find("sku", "MUG-001").firstResultOptional().isPresent());
        assertTrue(ShoppingCart.find("customerId", "cust-alice").firstResultOptional().isPresent());
        assertTrue(Order.find("orderNumber", "ORD-2026-0001").firstResultOptional().isPresent());
    }
}
