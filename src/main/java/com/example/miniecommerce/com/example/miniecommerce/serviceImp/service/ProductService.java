package com.example.miniecommerce.com.example.miniecommerce.serviceImp.service;

import com.example.miniecommerce.com.example.miniecommerce.dto.ProductRequest;
import com.example.miniecommerce.com.example.miniecommerce.dto.RegisterRequest;
import com.example.miniecommerce.com.example.miniecommerce.entity.Product;
import com.example.miniecommerce.com.example.miniecommerce.entity.User;
import com.example.miniecommerce.com.example.miniecommerce.enums.ProductCategory;
import com.example.miniecommerce.com.example.miniecommerce.repos.ProductRepo;
import com.example.miniecommerce.com.example.miniecommerce.repos.RoleRepo;
import com.example.miniecommerce.com.example.miniecommerce.repos.UserRepo;
import com.example.miniecommerce.com.example.miniecommerce.security.JWTUtil;
import com.example.miniecommerce.com.example.miniecommerce.serviceImp.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductServiceImpl {

    @Value("${role.admin}")
    private String adminRole;

    private ProductRepo productRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final JWTUtil jwtUtil;

    public ProductService(UserRepo userRepo, RoleRepo roleRepo, JWTUtil jwtUtil, ProductRepo productRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.productRepo = productRepo;
    }

    private RegisterRequest adminDetails(String token) {
        RegisterRequest adminDetails = new RegisterRequest();
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                if (jwtUtil.validateToken(jwtToken) && jwtUtil.extractRoles(jwtToken).contains(adminRole)) {

                    adminDetails.setUsername(jwtUtil.extractUsername(jwtToken));
                    adminDetails.setRoles(jwtUtil.extractRoles(jwtToken));
                    return adminDetails;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public ResponseEntity<String> addProduct(String token, ProductRequest productRequest) {
        RegisterRequest adminDetails = adminDetails(token);
        if (adminDetails == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Your not a Admin");
        }
        Product product = new Product();
        try {
            product.setProductName(productRequest.getProductName());
            product.setProductDescription(productRequest.getProductDescription());
            product.setProductPrice(productRequest.getProductPrice());
            product.setProductCategory(
                    ProductCategory.valueOf(
                            String.valueOf(productRequest
                                    .getProductCategory()).toUpperCase()));
            product.setStockQuantity(productRequest.getStockQuantity());
            User user = userRepo.findByUsername(adminDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            product.setCreatedBy(user);
            product.setModifiedBy(user);
            productRepo.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added product");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Not saved");
        }
    }
}
