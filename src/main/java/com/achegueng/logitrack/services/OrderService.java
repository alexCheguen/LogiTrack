package com.achegueng.logitrack.services;

import com.achegueng.logitrack.enums.order_status;
import com.achegueng.logitrack.models.Customer;
import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.models.OrderItem;
import com.achegueng.logitrack.repositories.CustomerRepository;
import com.achegueng.logitrack.repositories.OrderRepository;
import com.achegueng.logitrack.repositories.OrderItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private OrderItemRepository orderItemRepository;

    // Crear orden
    @Transactional
    public Optional<Order> createOrder(Order order) {
        // Validar cliente
        Optional<Customer> customerOpt = customerRepository.findById(order.getCustomer().getCustomerId());
        if (customerOpt.isEmpty() || !Boolean.TRUE.equals(customerOpt.get().getActive())) {
            throw new IllegalStateException("Cliente inválido o inactivo, no puede crear órdenes");
        }

        // Estado inicial siempre Pending
        if (order.getStatus() == null) {
            order.setStatus(order_status.Pending);
        }

        // Calcular total
        BigDecimal total = calculateTotal(order.getItems());
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    // Obtener todas las órdenes
    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    // Buscar por ID
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    // Actualizar estado de la orden
    @Transactional
    public Optional<Order> updateStatus(Long id, order_status status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return Optional.empty();
    }

    // Calcular total de la orden
    private BigDecimal calculateTotal(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Eliminar orden
    @Transactional
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}

