package com.example.resource;

import com.example.dto.ShoppingCartDto;
import com.example.model.ShoppingCart;
import com.example.repository.ShoppingCartRepository;
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

import java.time.Instant;
import java.util.List;

@Path("/api/shopping-carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartResource {

    @Inject
    ShoppingCartRepository shoppingCartRepository;

    @GET
    public List<ShoppingCartDto> list() {
        return shoppingCartRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public ShoppingCartDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(ShoppingCartDto dto) {
        ShoppingCart cart = fromDto(new ShoppingCart(), dto);
        Instant now = Instant.now();
        cart.createdAt = now;
        cart.updatedAt = now;
        shoppingCartRepository.persist(cart);
        return Response.status(Response.Status.CREATED).entity(toDto(cart)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ShoppingCartDto update(@PathParam("id") Long id, ShoppingCartDto dto) {
        ShoppingCart cart = find(id);
        fromDto(cart, dto);
        cart.updatedAt = Instant.now();
        return toDto(cart);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!shoppingCartRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private ShoppingCart find(Long id) {
        return shoppingCartRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private ShoppingCartDto toDto(ShoppingCart cart) {
        return new ShoppingCartDto(cart.id, cart.customerId, cart.createdAt, cart.updatedAt);
    }

    private ShoppingCart fromDto(ShoppingCart cart, ShoppingCartDto dto) {
        cart.customerId = dto.customerId();
        return cart;
    }
}
