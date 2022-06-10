package com.example.shopjpa.entities;

import com.example.shopjpa.dto.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, String> {
    Stream<ProductResponse> findByName(String name);
}
