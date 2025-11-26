package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.OrderItem;
import com.achegueng.logitrack.services.OrderItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/order-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderItemController {

    @Inject
    private OrderItemService orderItemService;

    // Agregar item a una orden
    @POST
    @Path("/add")
    public Response addItem(@QueryParam("orderId") Long orderId,
                            @QueryParam("productId") Long productId,
                            @QueryParam("quantity") Integer quantity) {
        try {
            Optional<OrderItem> newItem = orderItemService.addItem(orderId, productId, quantity);
            return Response.status(Response.Status.CREATED).entity(newItem.get()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Listar items de una orden
    @GET
    @Path("/order/{orderId}")
    public Response getItemsByOrder(@PathParam("orderId") Long orderId) {
        List<OrderItem> items = orderItemService.getItemsByOrder(orderId);
        if (items.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No hay items para esta orden").build();
        }
        return Response.ok(items).build();
    }

    // Eliminar item
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        orderItemService.deleteItem(id);
        return Response.status(Response.Status.NO_CONTENT).entity("Item eliminado").build();
    }
}

