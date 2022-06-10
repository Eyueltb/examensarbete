package com.fluxshop.services;

import com.fluxshop.dto.ProductRequest;
import com.fluxshop.dto.ProductResponse;
import com.fluxshop.entities.Product;
import com.fluxshop.entities.ProductRepository;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Flux<ProductResponse> all() {
        return repository.findAll().map(ProductResponse::fromModel);
    }

    public Mono<ProductResponse> create(ProductRequest product) {
        return repository.save(new Product(product.getName(), product.getDescription(), product.getPrice())).map(ProductResponse::fromModel);
    }

    public Flux<ProductResponse> add(Publisher<Product> products) {
         return repository.saveAll(products).map(ProductResponse::fromModel);
    }

    public Mono<ProductResponse> get(String id) {
        return repository.findById(id).map(ProductResponse::fromModel);
    }

    public Mono<ProductResponse> update(String id, ProductRequest product) {
        return repository.findById(id)
                .flatMap(prod -> {
                    prod.setName(product.getName());
                    prod.setDescription(product.getDescription());
                    prod.setPrice(product.getPrice());
                    return repository.save(prod).map(ProductResponse::fromModel);
                });
    }

    public Flux<ProductResponse> findByName(String name){
        return repository.findByName(name);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }
}
