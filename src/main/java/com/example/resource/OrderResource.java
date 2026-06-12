package com.example.resource;

import com.example.dto.OrderDto;
import com.example.model.Order;
import com.example.model.OrderStatus;
import com.example.repository.OrderRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderRepository orderRepository;

    @GET
    public List<OrderDto> list() {
        return orderRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public OrderDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(OrderDto dto) {
        Order order = fromDto(new Order(), dto);
        order.createdAt = Instant.now();
        orderRepository.persist(order);
        return Response.status(Response.Status.CREATED).entity(toDto(order)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public OrderDto update(@PathParam("id") Long id, OrderDto dto) {
        Order order = find(id);
        fromDto(order, dto);
        return toDto(order);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!orderRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private Order find(Long id) {
        return orderRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.id,
                order.orderNumber,
                order.status,
                order.totalAmount,
                order.createdAt
        );
    }

    private Order fromDto(Order order, OrderDto dto) {
        order.orderNumber = dto.orderNumber();
        order.status = dto.status() != null ? dto.status() : OrderStatus.PENDING;
        order.totalAmount = dto.totalAmount() != null ? dto.totalAmount() : BigDecimal.ZERO;
        return order;
    }
}
