package com.achegueng.logitrack.services;

import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.models.Payment;
import com.achegueng.logitrack.repositories.OrderRepository;
import com.achegueng.logitrack.repositories.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private OrderRepository orderRepository;

    // Registrar pago
    @Transactional
    public Optional<Payment> registerPayment(Payment payment) {
        // Validar orden
        Optional<Order> orderOpt = orderRepository.findById(payment.getOrderId().getOrderId());
        if (orderOpt.isEmpty()) {
            throw new IllegalStateException("Orden no encontrada");
        }

        Order order = orderOpt.get();

        // Calcular total pagado hasta ahora
        BigDecimal totalPagado = paymentRepository.findByOrderId(order.getOrderId())
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendiente = order.getTotalAmount().subtract(totalPagado);

        // Validar monto
        if (payment.getAmount().compareTo(pendiente) > 0) {
            throw new IllegalStateException("El monto excede el total pendiente de la orden");
        }

        // Fecha automática
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        return paymentRepository.save(payment);
    }

    // Consultar pagos por orden
    public List<Payment> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // Consultar pago por ID
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }
}

