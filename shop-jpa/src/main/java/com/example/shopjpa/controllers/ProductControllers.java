package com.example.shopjpa.controllers;


import com.example.shopjpa.dto.ProductRequest;
import com.example.shopjpa.dto.ProductResponse;
import com.example.shopjpa.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductControllers {
    private final ProductService productService;

    @GetMapping
    public Stream<ProductResponse> all() {
        return productService.all();
    }

    @PostMapping
    public Optional<ProductResponse> create(@RequestBody() ProductRequest product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    public Optional<ProductResponse> get(@PathVariable("id") String id) {
        return productService.get(id);
    }

    @PutMapping("/{id}")
    public Optional<ProductResponse> update(@PathVariable("id") String id, @RequestBody() ProductRequest product) {
        return productService.update(id, product);
    }

    @GetMapping("/search/{name}")
    public Stream<ProductResponse> searchByGameStatus(@PathVariable String name) {
        return productService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        productService.delete(id);
    }

    @DeleteMapping
    public void deleteAll() { productService.deleteAll();   }

}
