package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.enums.order_status;
import com.achegueng.logitrack.services.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    private OrderService orderService;

    // Obtener todas las órdenes
    @GET
    public Response getOrders() {
        return Response.ok(orderService.getAll()).build();
    }

    // Obtener orden por ID
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Orden no encontrada").build();
        }
        return Response.ok(order.get()).build();
    }

    // Crear orden
    @POST
    public Response createOrder(Order order) {
        try {
            Optional<Order> newOrder = orderService.createOrder(order);
            if (newOrder.isEmpty()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al crear orden").build();
            }
            return Response.status(Response.Status.CREATED).entity(newOrder.get()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Actualizar estado de la orden
    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status) {
        try {
            order_status newStatus = order_status.valueOf(status.toUpperCase());
            Optional<Order> updatedOrder = orderService.updateStatus(id, newStatus);

            if (updatedOrder.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Orden no encontrada").build();
            }
            return Response.ok(updatedOrder.get()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Estado inválido").build();
        }
    }

    // Eliminar orden
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Orden no encontrada").build();
        }
        orderService.delete(orderOpt.get());
        return Response.status(Response.Status.NO_CONTENT).entity("Orden eliminada").build();
    }
}

