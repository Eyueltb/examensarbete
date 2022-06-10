package com.fluxshop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
@Value
public class ProductApi {
    private static final Logger logger = LoggerFactory.getLogger(ProductApi.class);
    private static final String BASE_URL = "http://localhost:8081/api/products/";
    WebTestClient webTestClient ;

    public Flux<Product> all() {
        return webTestClient.get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody();
    }



    public Mono<Product> get(String id) {
        return webTestClient.get().uri(BASE_URL + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody()
                .single();
    }

    public Mono<Product> create(String name, String description, BigDecimal price) {
        return webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new CreateProduct(name, description, price)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody()
                .single();
    }

    public Mono<Product> update(String id, String name, String description, BigDecimal price) {
        return webTestClient.put().uri(BASE_URL + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UpdateProduct(name, description, price)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Product.class)
                .getResponseBody()
                .single();
    }

    public Mono<Void> delete(String id) {
        return webTestClient.delete().uri(BASE_URL + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseBody()
                .then();
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
