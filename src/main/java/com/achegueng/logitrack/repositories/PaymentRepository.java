package com.achegueng.logitrack.repositories;

import com.achegueng.logitrack.models.Payment;
import com.smarroquin.logitrack.models.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaymentRepository extends BaseRepository<Payment, Long> {

    @Override
    protected Class<Payment> entity() {
        return Payment.class;
    }

    public List<Payment> findByOrderId(Long orderId) {
        return em.createQuery(
                        "SELECT p FROM Payment p WHERE p.orderId.orderId = :orderId", Payment.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    // Buscar pagos por cliente
    public List<Payment> findByCustomerId(Long customerId) {
        return em.createQuery(
                        "SELECT p FROM Payment p WHERE p.orderId.customer.customerId = :customerId", Payment.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

}
