package com.example.resource;

import com.example.dto.ProductDto;
import com.example.model.Product;
import com.example.repository.ProductRepository;
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

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductRepository productRepository;

    @GET
    public List<ProductDto> list() {
        return productRepository.listAll().stream().map(this::toDto).toList();
    }

    @GET
    @Path("/{id}")
    public ProductDto get(@PathParam("id") Long id) {
        return toDto(find(id));
    }

    @POST
    @Transactional
    public Response create(ProductDto dto) {
        Product product = fromDto(new Product(), dto);
        product.createdAt = Instant.now();
        productRepository.persist(product);
        return Response.status(Response.Status.CREATED).entity(toDto(product)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public ProductDto update(@PathParam("id") Long id, ProductDto dto) {
        Product product = find(id);
        fromDto(product, dto);
        return toDto(product);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        if (!productRepository.deleteById(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }

    private Product find(Long id) {
        return productRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.id,
                product.name,
                product.description,
                product.price,
                product.sku,
                product.stockQuantity,
                product.createdAt
        );
    }

    private Product fromDto(Product product, ProductDto dto) {
        product.name = dto.name();
        product.description = dto.description();
        product.price = dto.price();
        product.sku = dto.sku();
        product.stockQuantity = dto.stockQuantity() != null ? dto.stockQuantity() : 0;
        return product;
    }
}
