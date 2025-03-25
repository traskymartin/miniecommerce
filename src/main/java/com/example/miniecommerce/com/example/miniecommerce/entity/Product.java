package com.example.miniecommerce.com.example.miniecommerce.entity;

import com.example.miniecommerce.com.example.miniecommerce.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")  // Naming table explicitly
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Product name should be unique
    private String productName;

    @Column(nullable = false)
    private String productDescription;

    @Column(nullable = false, precision = 10, scale = 2) // For money handling
    private BigDecimal productPrice;

    @Enumerated(EnumType.STRING) // Use enum for categories
    @Column(nullable = false)
    private ProductCategory productCategory;

//    private String productImage; // URL or file path

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id", updatable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "modified_by_admin_id")
    private User modifiedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
