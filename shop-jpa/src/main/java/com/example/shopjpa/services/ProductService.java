package com.example.shopjpa.services;


import com.example.shopjpa.dto.ProductRequest;
import com.example.shopjpa.dto.ProductResponse;
import com.example.shopjpa.entities.Product;
import com.example.shopjpa.entities.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Stream<ProductResponse> all() {
        return productRepository.findAll().stream().map(ProductResponse::fromModel);
    }

    public Optional<ProductResponse> create(ProductRequest product) {
        return Optional.of(productRepository.save(new Product(product.getName(), product.getDescription(), product.getPrice())))
                .map(ProductResponse::fromModel);
    }

    public Optional<ProductResponse> get(String id) {
        return productRepository.findById(id).map(ProductResponse::fromModel);
    }

    public Optional<ProductResponse> update(String id, ProductRequest product) {
        return productRepository.findById(id)
                .flatMap(prod -> {
                    prod.setName(product.getName());
                    prod.setDescription(product.getDescription());
                    prod.setPrice(product.getPrice());
                    return Optional.of(productRepository.save(prod)).map(ProductResponse::fromModel);
                });
    }

    public Stream<ProductResponse> findByName(String name){
        return productRepository.findByName(name);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }
}
