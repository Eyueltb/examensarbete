package com.example.shopjpa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class ProductApi {
    private static final Logger logger = LoggerFactory.getLogger(ProductApi.class);
    private static final String BASE_URL = "http://localhost:8084/api/products/";

    WebTestClient webTestClient ;


    public List<Product> all() {
        return webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody().toStream().collect(Collectors.toList());
    }

    public Product get(String id) {
        return webTestClient.get().uri(BASE_URL + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody()
                .toStream().findFirst().get();
    }

    public Product create(String name, String description, BigDecimal price) {
        return null;
    }

    public Product update(String id, String name, String description, BigDecimal price) {
        return null;
    }

    public void delete(String id) {

    }

    @Value
    static class Product {
        String id;
        String name;
        String description;
        BigDecimal price;
        @JsonCreator
        public Product(
                @JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("price") BigDecimal price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }
    @Value
    static class CreateProduct {
        String name;
        String description;
        BigDecimal price;
    }

    @Value
    static class UpdateProduct {
        String name;
        String description;
        BigDecimal price;
    }


}
