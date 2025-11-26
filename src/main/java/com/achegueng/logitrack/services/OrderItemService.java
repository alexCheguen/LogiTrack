package com.achegueng.logitrack.services;

import com.achegueng.logitrack.models.Order;
import com.achegueng.logitrack.models.OrderItem;
import com.achegueng.logitrack.models.Product;
import com.achegueng.logitrack.repositories.OrderItemRepository;
import com.achegueng.logitrack.repositories.OrderRepository;
import com.achegueng.logitrack.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderItemService {

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private ProductRepository productRepository;

    // Agregar item a una orden
    @Transactional
    public Optional<OrderItem> addItem(Long orderId, Long productId, Integer quantity) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (orderOpt.isEmpty() || productOpt.isEmpty()) {
            throw new IllegalStateException("Orden o producto no encontrado");
        }

        Product product = productOpt.get();
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalStateException("Producto inactivo, no puede agregarse a la orden");
        }

        OrderItem item = new OrderItem();
        item.setOrderId(orderOpt.get());
        item.setProductId(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        return orderItemRepository.save(item);
    }

    // Listar items de una orden
    public List<OrderItem> getItemsByOrder(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    // Eliminar item
    @Transactional
    public void deleteItem(Long id) {
        Optional<OrderItem> itemOpt = orderItemRepository.findById(id);
        itemOpt.ifPresent(orderItemRepository::delete);
    }
}

