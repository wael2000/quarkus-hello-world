package com.example.resource;

import com.example.dto.PaymentDto;
import com.example.model.Payment;
import com.example.model.PaymentStatus;
import com.example.repository.PaymentRepository;
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

@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    EntityLookup entityLookup;

    @GET
    public List<PaymentDto> list() {
        return paymentRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public PaymentDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(PaymentDto dto) {
        Payment payment = fromDto(new Payment(), dto);
        paymentRepository.persist(payment);
        return Response.status(Response.Status.CREATED).entity(toDto(payment)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public PaymentDto update(@PathParam("id") Long id, PaymentDto dto) {
        Payment payment = find(id);
        fromDto(payment, dto);
        return toDto(payment);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!paymentRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private Payment find(Long id) {
        return paymentRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.id,
                payment.order != null ? payment.order.id : null,
                payment.checkout != null ? payment.checkout.id : null,
                payment.amount,
                payment.method,
                payment.status,
                payment.transactionId,
                payment.paidAt
        );
    }

    private Payment fromDto(Payment payment, PaymentDto dto) {
        if (dto.orderId() == null || dto.checkoutId() == null) {
            throw new BadRequestException("orderId and checkoutId are required");
        }
        payment.order = entityLookup.requireOrder(dto.orderId());
        payment.checkout = entityLookup.requireCheckout(dto.checkoutId());
        payment.amount = dto.amount();
        payment.method = dto.method();
        payment.status = dto.status() != null ? dto.status() : PaymentStatus.PENDING;
        payment.transactionId = dto.transactionId();
        payment.paidAt = dto.paidAt();
        return payment;
    }
}
