package com.example.service;

import com.example.model.Checkout;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.ShoppingCart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EntityLookup {

    public Product requireProduct(Long id) {
        return Product.<Product>findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    public ShoppingCart requireShoppingCart(Long id) {
        return ShoppingCart.<ShoppingCart>findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    public Order requireOrder(Long id) {
        return Order.<Order>findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    public Checkout requireCheckout(Long id) {
        return Checkout.<Checkout>findByIdOptional(id).orElseThrow(NotFoundException::new);
    }
}
