package com.achegueng.logitrack.repositories;

import com.achegueng.logitrack.enums.category_type;
import com.achegueng.logitrack.models.Product;
import com.smarroquin.logitrack.models.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository extends BaseRepository<Product, Long> {

    @Override
    protected Class<Product> entity() {
        return Product.class;
    }

    public List<Product> findByCategory(category_type category) {
        return em.createQuery(
                        "SELECT p FROM Product p WHERE p.category = :category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Product> findByActiveTrue() {
        return em.createQuery(
                        "SELECT p FROM Product p WHERE p.active = true", Product.class)
                .getResultList();
    }
}
