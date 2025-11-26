package com.achegueng.logitrack.services;

import com.achegueng.logitrack.models.Product;
import com.achegueng.logitrack.enums.category_type;
import com.achegueng.logitrack.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    // Registrar producto
    @Transactional
    public Optional<Product> create(Product product) {
        if (product.getActive() == null) {
            product.setActive(true); // por defecto activo
        }
        return productRepository.save(product);
    }

    // Actualizar producto
    @Transactional
    public Optional<Product> update(Product product) {
        return productRepository.save(product);
    }

    // Listar todos los productos
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    // Buscar por ID
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // Consultar por categoría
    public List<Product> findByCategory(category_type category) {
        return productRepository.findByCategory(category);
    }

    // Marcar producto como activo/inactivo
    @Transactional
    public Optional<Product> changeStatus(Long id, boolean active) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setActive(active);
            return productRepository.save(product);
        }
        return Optional.empty();
    }

    // Eliminar producto
    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }
}

