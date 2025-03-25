package com.example.miniecommerce.com.example.miniecommerce.serviceImp;

import com.example.miniecommerce.com.example.miniecommerce.dto.ProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductServiceImpl {

    ResponseEntity<String> addProduct(String token, ProductRequest productRequest);
    ResponseEntity<String> updateProduct(String token, ProductRequest productRequest);
}
