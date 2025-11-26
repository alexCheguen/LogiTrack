package com.achegueng.logitrack.controllers;

import com.achegueng.logitrack.models.Customer;
import com.achegueng.logitrack.services.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    private CustomerService customerService;

    // Obtener todos los clientes
    @GET
    public Response getCustomers() {
        return Response.ok(customerService.getAll()).build();
    }

    // Obtener cliente por ID
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado").build();
        }
        return Response.ok(customer.get()).build();
    }

    // Consultar cliente por NIT
    @GET
    @Path("/nit/{nit}")
    public Response getByNIT(@PathParam("nit") String nit) {
        Optional<Customer> customer = customerService.findByNIT(nit);
        if (!customer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente con NIT " + nit + " no encontrado").build();
        }
        return Response.ok(customer.get()).build();
    }


    // Registrar cliente
    @POST
    public Response createCustomer(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("El nombre del cliente es requerido").build();
        }

        Optional<Customer> newCustomer = customerService.create(customer);

        if (!newCustomer.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al registrar cliente").build();
        }

        return Response.status(Response.Status.CREATED).entity(newCustomer.get()).build();
    }

    // Actualizar cliente
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        Optional<Customer> optionalCustomer = customerService.findById(id);

        if (!optionalCustomer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado").build();
        }

        Customer customerToUpdate = optionalCustomer.get();
        customerToUpdate.setFullName(customer.getFullName());
        customerToUpdate.setNIT(customer.getNIT());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setAddress(customer.getAddress());
        customerToUpdate.setActive(customer.getActive());

        Optional<Customer> updatedCustomer = customerService.update(customerToUpdate);

        return Response.ok(updatedCustomer.get()).build();
    }

    // Desactivar cliente
    @PUT
    @Path("/{id}/deactivate")
    public Response deactivateCustomer(@PathParam("id") Long id) {
        Optional<Customer> updatedCustomer = customerService.deactivate(id);

        if (!updatedCustomer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado").build();
        }

        return Response.ok(updatedCustomer.get()).build();
    }

    // Eliminar cliente
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        Optional<Customer> optionalCustomer = customerService.findById(id);

        if (!optionalCustomer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado").build();
        }

        customerService.delete(optionalCustomer.get());
        return Response.status(Response.Status.NO_CONTENT).entity("Cliente eliminado").build();
    }
}


