package com.example.resource;

import com.example.dto.LineItemDto;
import com.example.model.LineItem;
import com.example.repository.LineItemRepository;
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

@Path("/api/line-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LineItemResource {

    @Inject
    LineItemRepository lineItemRepository;

    @Inject
    EntityLookup entityLookup;

    @GET
    public List<LineItemDto> list() {
        return lineItemRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public LineItemDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(LineItemDto dto) {
        LineItem lineItem = fromDto(new LineItem(), dto);
        lineItemRepository.persist(lineItem);
        return Response.status(Response.Status.CREATED).entity(toDto(lineItem)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public LineItemDto update(@PathParam("id") Long id, LineItemDto dto) {
        LineItem lineItem = find(id);
        fromDto(lineItem, dto);
        return toDto(lineItem);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!lineItemRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private LineItem find(Long id) {
        return lineItemRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private LineItemDto toDto(LineItem lineItem) {
        return new LineItemDto(
                lineItem.id,
                lineItem.product != null ? lineItem.product.id : null,
                lineItem.quantity,
                lineItem.unitPrice,
                lineItem.shoppingCart != null ? lineItem.shoppingCart.id : null,
                lineItem.order != null ? lineItem.order.id : null
        );
    }

    private LineItem fromDto(LineItem lineItem, LineItemDto dto) {
        if (dto.productId() == null) {
            throw new BadRequestException("productId is required");
        }
        boolean hasCart = dto.shoppingCartId() != null;
        boolean hasOrder = dto.orderId() != null;
        if (hasCart == hasOrder) {
            throw new BadRequestException("Exactly one of shoppingCartId or orderId is required");
        }

        lineItem.product = entityLookup.requireProduct(dto.productId());
        lineItem.quantity = dto.quantity();
        lineItem.unitPrice = dto.unitPrice();
        lineItem.shoppingCart = hasCart ? entityLookup.requireShoppingCart(dto.shoppingCartId()) : null;
        lineItem.order = hasOrder ? entityLookup.requireOrder(dto.orderId()) : null;
        return lineItem;
    }
}
