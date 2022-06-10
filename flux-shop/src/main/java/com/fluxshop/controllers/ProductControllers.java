package com.fluxshop.controllers;

import com.fluxshop.dto.ProductRequest;
import com.fluxshop.dto.ProductResponse;
import com.fluxshop.entities.Product;
import com.fluxshop.services.ProductService;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductControllers {
    private final ProductService productService;

    @GetMapping
    public Flux<ProductResponse> all() {
        return productService.all();
    }

    @PostMapping
    public Mono<ProductResponse> create(@RequestBody() ProductRequest product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    public Mono<ProductResponse> get(@PathVariable("id") String id) {
        return productService.get(id);
    }

    @PutMapping("/{id}")
    public Mono<ProductResponse> update(@PathVariable("id") String id, @RequestBody() ProductRequest product) {
        return productService.update(id, product);
    }

    @PostMapping("/products")
    public Flux<ProductResponse> add(@RequestBody Publisher<Product> products){
        return  productService.add(products);
    }

    @GetMapping("/search/{name}")
    public Flux<ProductResponse> searchByGameStatus(@PathVariable String name) {
        return productService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return productService.delete(id);
    }

    @DeleteMapping
    public Mono<Void> deleteAll() { return productService.deleteAll();   }

}
