package com.example.miniecommerce.com.example.miniecommerce.controller;

import com.example.miniecommerce.com.example.miniecommerce.dto.ProductRequest;
import com.example.miniecommerce.com.example.miniecommerce.serviceImp.ProductServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestHeader("Authorization") String token, @RequestBody ProductRequest productRequest) {
        return productService.addProduct(token,productRequest);
    }

}
