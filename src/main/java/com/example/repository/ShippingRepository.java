package com.example.repository;

import com.example.model.Shipping;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShippingRepository implements PanacheRepository<Shipping> {
}
