package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.services.ReportService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportController {

    @Inject
    private ReportService reportService;

    // Total adeudado por cliente
    @GET
    @Path("/debt/{customerId}")
    public Response getTotalAdeudado(@PathParam("customerId") Long customerId) {
        BigDecimal deuda = reportService.getTotalAdeudado(customerId);
        return Response.ok(deuda).build();
    }

    // Órdenes incompletas
    @GET
    @Path("/orders/incomplete")
    public Response getIncompleteOrders() {
        List<Order> orders = reportService.getIncompleteOrders();
        return Response.ok(orders).build();
    }

    // Productos más vendidos
    @GET
    @Path("/products/top")
    public Response getTopProducts() {
        List<Object[]> topProducts = reportService.getTopProducts();
        return Response.ok(topProducts).build();
    }
}

