package com.achegueng.logitrack.services;

import com.achegueng.logitrack.models.Customer;
import com.achegueng.logitrack.repositories.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    // Registrar cliente
    @Transactional
    public Optional<Customer> create(Customer customer) {
        if (customer.getActive() == null) {
            customer.setActive(true); // por defecto activo
        }
        return customerRepository.save(customer);
    }

    // Actualizar cliente
    @Transactional
    public Optional<Customer> update(Customer customer) {
        return customerRepository.save(customer);
    }

    // Consultar todos
    public List<Customer> getAll() {
        return customerRepository.getAll();
    }

    // Consultar por ID
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    // Consultar por NIT (taxId)
    public Optional<Customer> findByNIT(String nit) {
        return customerRepository.findByNIT(nit);
    }

    // Desactivar cliente
    @Transactional
    public Optional<Customer> deactivate(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setActive(false);
            return customerRepository.save(customer);
        }
        return Optional.empty();
    }

    // Eliminar cliente
    @Transactional
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }
}

