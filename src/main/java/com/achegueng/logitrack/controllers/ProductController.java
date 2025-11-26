package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.Product;
import com.achegueng.logitrack.enums.category_type;
import com.achegueng.logitrack.services.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    // Listar todos los productos
    @GET
    public Response getProducts() {
        return Response.ok(productService.getAll()).build();
    }

    // Obtener producto por ID
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }
        return Response.ok(product.get()).build();
    }

    // Consultar productos por categoría
    @GET
    @Path("/category/{category}")
    public Response getByCategory(@PathParam("category") String category) {
        try {
            category_type cat = category_type.valueOf(category.toUpperCase());
            List<Product> products = productService.findByCategory(cat);
            if (products.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No hay productos en la categoría " + category).build();
            }
            return Response.ok(products).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Categoría inválida").build();
        }
    }

    // Registrar producto
    @POST
    public Response createProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El nombre del producto es requerido").build();
        }

        Optional<Product> newProduct = productService.create(product);

        if (newProduct.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al registrar producto").build();
        }

        return Response.status(Response.Status.CREATED).entity(newProduct.get()).build();
    }

    // Actualizar producto
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (optionalProduct.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }

        Product productToUpdate = optionalProduct.get();
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setActive(product.getActive());

        Optional<Product> updatedProduct = productService.update(productToUpdate);

        return Response.ok(updatedProduct.get()).build();
    }

    // Cambiar estado (activo/inactivo)
    @PUT
    @Path("/{id}/status")
    public Response changeStatus(@PathParam("id") Long id, @QueryParam("active") boolean active) {
        Optional<Product> updatedProduct = productService.changeStatus(id, active);

        if (updatedProduct.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }

        return Response.ok(updatedProduct.get()).build();
    }

    // Eliminar producto
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (optionalProduct.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }

        productService.delete(optionalProduct.get());
        return Response.status(Response.Status.NO_CONTENT).entity("Producto eliminado").build();
    }
}
