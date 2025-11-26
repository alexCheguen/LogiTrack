package com.achegueng.logitrack.services;

import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.models.Payment;
import com.achegueng.logitrack.repositories.OrderRepository;
import com.achegueng.logitrack.repositories.PaymentRepository;
import com.achegueng.logitrack.repositories.OrderItemRepository;
import com.achegueng.logitrack.enums.order_status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ReportService {

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private OrderItemRepository orderItemRepository;

    // Total adeudado por cliente
    public BigDecimal getTotalAdeudado(Long customerId) {
        BigDecimal totalOrders = orderRepository.findByCustomerId(customerId)
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalPagos = paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        return totalOrders.subtract(totalPagos);
    }

    // Órdenes incompletas
    public List<Order> getIncompleteOrders() {
        return orderRepository.findByStatus(order_status.Pending, order_status.Completed, order_status.Cancelled, order_status.Processing);
    }

    // Productos más vendidos
    public List<Object[]> getTopProducts() {
        return orderItemRepository.findTopProducts();
    }
}

