package com.quickpay.backend.controller;

import com.quickpay.backend.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public List<Product> getProducts() {
        return List.of(
                new Product(1L, "Laptop", "A powerful laptop", new BigDecimal("1200.00"), null),
                new Product(2L, "Smartphone", "A modern smartphone", new BigDecimal("800.00"), null),
                new Product(3L, "Headphones", "Noise-cancelling headphones", new BigDecimal("250.00"), null)
        );
    }
}
