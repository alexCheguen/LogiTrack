package com.achegueng.logitrack.repositories;

import com.achegueng.logitrack.models.Customer;
import com.smarroquin.logitrack.models.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepository extends BaseRepository<Customer, Long> {

    @Override
    protected Class<Customer> entity() {
        return Customer.class;
    }

    public Optional<Customer> findByNIT(String nit) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.NIT = :nit", Customer.class)
                .setParameter("nit", nit)
                .getResultStream()
                .findFirst();
    }
}
