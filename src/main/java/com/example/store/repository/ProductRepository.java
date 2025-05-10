package com.example.store.repository;

import com.example.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProductRepository class.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product if it contains provided value in its title.
     * @param title String - provided value
     * @return List of products
     */
    List<Product> findByTitleContaining(String title);
}
