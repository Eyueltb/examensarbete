package com.fluxshop.tests;

import com.fluxshop.dto.ProductResponse;
import com.fluxshop.entities.Product;
import com.fluxshop.entities.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ProductApi {
    private static final String BASE_URL = "http://localhost:8082/api";
    private static WebClient client = WebClient.create(BASE_URL);

    @Autowired
    private ProductRepository repository = new ProductRepository() {
        @Override
        public Flux<ProductResponse> findByName(String name) {
            return repository.findByName(name);
        }

        @Override
        public <S extends Product> Mono<S> save(S entity) {
            return repository.save(entity);
        }

        @Override
        public <S extends Product> Flux<S> saveAll(Iterable<S> entities) {
            return repository.saveAll(entities);
        }

        @Override
        public <S extends Product> Flux<S> saveAll(Publisher<S> entityStream) {
            return repository.saveAll(entityStream);
        }

        @Override
        public Mono<Product> findById(String s) {
            return repository.findById(s);
        }

        @Override
        public Mono<Product> findById(Publisher<String> id) {
            return repository.findById(id);
        }

        @Override
        public Mono<Boolean> existsById(String s) {
            return repository.existsById(s);
        }

        @Override
        public Mono<Boolean> existsById(Publisher<String> id) {
            return repository.existsById(id);
        }

        @Override
        public Flux<Product> findAll() {
            return repository.findAll();
        }

        @Override
        public Flux<Product> findAllById(Iterable<String> strings) {
            return repository.findAllById(strings);
        }

        @Override
        public Flux<Product> findAllById(Publisher<String> idStream) {
            return repository.findAllById(idStream);
        }

        @Override
        public Mono<Long> count() {
            return repository.count();
        }

        @Override
        public Mono<Void> deleteById(String s) {
            return repository.deleteById(s);
        }

        @Override
        public Mono<Void> deleteById(Publisher<String> id) {
            return repository.deleteById(id);
        }

        @Override
        public Mono<Void> delete(Product entity) {
            return repository.delete(entity);
        }

        @Override
        public Mono<Void> deleteAllById(Iterable<? extends String> strings) {
            return repository.deleteAllById(strings);
        }
        @Override
        public Mono<Void> deleteAll(Iterable<? extends Product> entities) {
            return repository.deleteAll(entities);
        }

        @Override
        public Mono<Void> deleteAll(Publisher<? extends Product> entityStream) {
            return repository.deleteAll(entityStream);
        }

        @Override
        public Mono<Void> deleteAll() {
            return repository.deleteAll();
        }
    };


    public void all(){
        client.get().uri("")
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    public void get(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        client.get().uri("/" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    public void create(){
        //Publisher<Product> products = subscriber -> List.of(new Product("Prod1", "desc1", BigDecimal.valueOf(90.34)),  new ProductRequest("Prod2", "desc2", BigDecimal.valueOf(120.34)), new ProductRequest("Prod3", "desc3", BigDecimal.valueOf(1290.34)));
        client.post().uri("")
                .bodyValue(new ProductRequest("Prod1", "desc1", BigDecimal.valueOf(90.34))).retrieve()
                .toBodilessEntity()
                .block();
    }

    public void update(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        client.put().uri("/" + id)
                .bodyValue(new ProductRequest("Prod1 modified ", "desc1 modified", BigDecimal.valueOf(90.34)))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    public void delete(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        client.delete().uri("/" +id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    public void deleteAll(){
        client.delete().uri("")
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
    }
}
