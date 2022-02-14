package com.example.webshop.model.repository;

import com.example.webshop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Product> findProductsByPriceHrkLessThan(double priceHrk);

    Collection<Product> findProductsByAvailable(boolean available);

    void deleteByCode(String code);
}
