package com.example.resource;

import com.example.dto.CheckoutDto;
import com.example.model.Checkout;
import com.example.model.CheckoutStatus;
import com.example.repository.CheckoutRepository;
import com.example.service.EntityLookup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
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

@Path("/api/checkouts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutResource {

    @Inject
    CheckoutRepository checkoutRepository;

    @Inject
    EntityLookup entityLookup;

    @GET
    public List<CheckoutDto> list() {
        return checkoutRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public CheckoutDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(CheckoutDto dto) {
        Checkout checkout = fromDto(new Checkout(), dto);
        checkout.createdAt = Instant.now();
        checkoutRepository.persist(checkout);
        return Response.status(Response.Status.CREATED).entity(toDto(checkout)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CheckoutDto update(@PathParam("id") Long id, CheckoutDto dto) {
        Checkout checkout = find(id);
        fromDto(checkout, dto);
        return toDto(checkout);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!checkoutRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private Checkout find(Long id) {
        return checkoutRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private CheckoutDto toDto(Checkout checkout) {
        return new CheckoutDto(
                checkout.id,
                checkout.shoppingCart != null ? checkout.shoppingCart.id : null,
                checkout.order != null ? checkout.order.id : null,
                checkout.status,
                checkout.createdAt,
                checkout.completedAt
        );
    }

    private Checkout fromDto(Checkout checkout, CheckoutDto dto) {
        if (dto.shoppingCartId() == null) {
            throw new BadRequestException("shoppingCartId is required");
        }
        checkout.shoppingCart = entityLookup.requireShoppingCart(dto.shoppingCartId());
        checkout.order = dto.orderId() != null ? entityLookup.requireOrder(dto.orderId()) : null;
        checkout.status = dto.status() != null ? dto.status() : CheckoutStatus.IN_PROGRESS;
        checkout.completedAt = dto.completedAt();
        return checkout;
    }
}
