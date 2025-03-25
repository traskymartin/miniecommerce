package com.example.miniecommerce.com.example.miniecommerce.repos;

import com.example.miniecommerce.com.example.miniecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    public List<Product> findByProductName(String productName);
}
