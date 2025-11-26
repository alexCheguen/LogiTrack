package com.achegueng.logitrack.repositories;

import com.achegueng.logitrack.models.OrderItem;
import com.smarroquin.logitrack.models.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {

    @Override
    protected Class<OrderItem> entity() {
        return OrderItem.class;
    }

    // Buscar items por orden
    public List<OrderItem> findByOrderId(Long orderId) {
        return em.createQuery(
                        "SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId = :orderId", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    // Productos más vendidos
    public List<Object[]> findTopProducts() {
        return em.createQuery(
                "SELECT oi.productId.name, SUM(oi.quantity) " +
                        "FROM OrderItem oi GROUP BY oi.productId.name ORDER BY SUM(oi.quantity) DESC",
                Object[].class
        ).getResultList();
    }
}
