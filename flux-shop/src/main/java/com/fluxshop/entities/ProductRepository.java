package com.fluxshop.entities;

import com.fluxshop.dto.ProductResponse;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    @Query("select * from Product p where p.name=:$name")
    Flux<ProductResponse> findByName(String name);

}
