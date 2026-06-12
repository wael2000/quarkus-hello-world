package com.example.repository;

import com.example.model.LineItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LineItemRepository implements PanacheRepository<LineItem> {
}
