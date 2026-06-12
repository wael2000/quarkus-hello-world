package com.example.repository;

import com.example.model.Checkout;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CheckoutRepository implements PanacheRepository<Checkout> {
}
