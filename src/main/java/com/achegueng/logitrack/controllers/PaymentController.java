package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.Payment;
import com.achegueng.logitrack.services.PaymentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    private PaymentService paymentService;

    // Registrar pago
    @POST
    public Response registerPayment(Payment payment) {
        try {
            Optional<Payment> newPayment = paymentService.registerPayment(payment);
            if (newPayment.isEmpty()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al registrar pago").build();
            }
            return Response.status(Response.Status.CREATED).entity(newPayment.get()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Consultar pagos por orden
    @GET
    @Path("/order/{orderId}")
    public Response getPaymentsByOrder(@PathParam("orderId") Long orderId) {
        List<Payment> payments = paymentService.getPaymentsByOrder(orderId);
        if (payments.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No hay pagos registrados para la orden").build();
        }
        return Response.ok(payments).build();
    }

    // Consultar pago por ID
    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") Long id) {
        Optional<Payment> payment = paymentService.findById(id);
        if (payment.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Pago no encontrado").build();
        }
        return Response.ok(payment.get()).build();
    }
}
