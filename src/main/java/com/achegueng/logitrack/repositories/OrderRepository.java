package com.achegueng.logitrack.repositories;

import com.achegueng.logitrack.enums.order_status;
import com.achegueng.logitrack.models.Order;
import com.smarroquin.logitrack.models.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderRepository extends BaseRepository<Order, Long> {

    @Override
    protected Class<Order> entity() {
        return Order.class;
    }

    // Buscar órdenes por cliente
    public List<Order> findByCustomerId(Long customerId) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.customer.customerId = :customerId", Order.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    // Buscar órdenes por estado
    public List<Order> findByStatus(order_status... statuses) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.status IN :statuses", Order.class)
                .setParameter("statuses", List.of(statuses))
                .getResultList();
    }
}

