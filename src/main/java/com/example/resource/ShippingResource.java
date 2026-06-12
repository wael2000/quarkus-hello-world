package com.example.resource;

import com.example.dto.ShippingDto;
import com.example.model.Shipping;
import com.example.model.ShippingStatus;
import com.example.repository.ShippingRepository;
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

import java.util.List;

@Path("/api/shipping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingResource {

    @Inject
    ShippingRepository shippingRepository;

    @Inject
    EntityLookup entityLookup;

    @GET
    public List<ShippingDto> list() {
        return shippingRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public ShippingDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(ShippingDto dto) {
        Shipping shipping = fromDto(new Shipping(), dto);
        shippingRepository.persist(shipping);
        return Response.status(Response.Status.CREATED).entity(toDto(shipping)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ShippingDto update(@PathParam("id") Long id, ShippingDto dto) {
        Shipping shipping = find(id);
        fromDto(shipping, dto);
        return toDto(shipping);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!shippingRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private Shipping find(Long id) {
        return shippingRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private ShippingDto toDto(Shipping shipping) {
        return new ShippingDto(
                shipping.id,
                shipping.order != null ? shipping.order.id : null,
                shipping.recipientName,
                shipping.addressLine1,
                shipping.addressLine2,
                shipping.city,
                shipping.postalCode,
                shipping.country,
                shipping.carrier,
                shipping.trackingNumber,
                shipping.status
        );
    }

    private Shipping fromDto(Shipping shipping, ShippingDto dto) {
        if (dto.orderId() == null) {
            throw new BadRequestException("orderId is required");
        }
        shipping.order = entityLookup.requireOrder(dto.orderId());
        shipping.recipientName = dto.recipientName();
        shipping.addressLine1 = dto.addressLine1();
        shipping.addressLine2 = dto.addressLine2();
        shipping.city = dto.city();
        shipping.postalCode = dto.postalCode();
        shipping.country = dto.country();
        shipping.carrier = dto.carrier();
        shipping.trackingNumber = dto.trackingNumber();
        shipping.status = dto.status() != null ? dto.status() : ShippingStatus.PENDING;
        return shipping;
    }
}
