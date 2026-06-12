package com.example.repository;

import com.example.model.ShoppingCart;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShoppingCartRepository implements PanacheRepository<ShoppingCart> {
}
