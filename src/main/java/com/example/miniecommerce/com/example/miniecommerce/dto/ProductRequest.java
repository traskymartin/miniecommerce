package com.example.miniecommerce.com.example.miniecommerce.dto;

import com.example.miniecommerce.com.example.miniecommerce.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Component
public class ProductRequest {

    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private ProductCategory productCategory;
    //    private String productImage; // URL or file path
    private Integer stockQuantity;
}
