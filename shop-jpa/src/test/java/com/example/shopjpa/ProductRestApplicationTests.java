package com.example.shopjpa;

import com.example.shopjpa.entities.Product;
import com.example.shopjpa.entities.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductRestApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ProductApi.class);
    private static WebClient client = WebClient.create("http://localhost:8081?delay=2");
    @Autowired WebTestClient webTestClient;
    @Autowired ProductRepository productRepository;
    ProductApi productApi;
    Product prod1;
    Product prod2;
    Product prod3;

    @BeforeEach
    void setUp() {
        webTestClient = webTestClient.mutate().build();
        productApi= new ProductApi(webTestClient);
        prod1 = new Product("prod1","description1", BigDecimal.valueOf(45.96));
        prod2 = new Product("prod2","description2", BigDecimal.valueOf(25.9623));
        prod3 = new Product("prod3","description3", BigDecimal.valueOf(15.96678));
    }
    @AfterEach
    void tearDown() { productRepository.deleteAll(); }
    @Test
    void test_all_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3));

        // When
        List<ProductApi.Product> products = productApi.all();

        // Then
        assertEquals(List.of("prod1", "prod2", "prod3"), products.stream().map(ProductApi.Product::getName).collect(Collectors.toList()));
    }
    @Test
    void test_get_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3));

        // When
        ProductApi.Product product = productApi.get(prod1.getId());

        // Then
        assertEquals("prod1", product.getName());
    }

    @Test
    void test_create_success() {
        // When
        ProductApi.Product product = productApi.create("prod4", "description4", BigDecimal.valueOf(45));

        // Then
        Product verifyProduct = productRepository.findAll().get(0);
        assertEquals(verifyProduct.getName(), product.getName());
        assertEquals(verifyProduct.getDescription(), product.getDescription());
        assertEquals(verifyProduct.getPrice(), product.getPrice());
        assertEquals(verifyProduct.getId(), product.getId());
    }

    @Test
    void test_update_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3));

        // When
        ProductApi.Product product = productApi.update(prod1.getId(), "prod1 modified","description1 modified", BigDecimal.valueOf(234.987) );

        // Then
        Product verifyGroup = productRepository.findById(prod1.getId()).get();
        assertEquals("prod1 modified", product.getName());
        assertEquals("prod1 modified", verifyGroup.getName());
    }

    @Test
    void test_delete_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3));

        // When
        productApi.delete(prod1.getId());

        // Then
        List<Product> verifyProduct = productRepository.findAll();
        assertEquals(List.of("prod1", "prod2"), verifyProduct.stream().map(Product::getName).collect(Collectors.toList()));
    }


    @Test
    void test_performance_all_success() {
        Instant start = Instant.now();
// Given
        productRepository.saveAll(List.of(prod1, prod2, prod3));

        // When
        List<ProductApi.Product> products = productApi.all();

        // Then
        assertEquals(List.of("prod1", "prod2", "prod3"), products.stream().map(ProductApi.Product::getName).collect(Collectors.toList()));

        logTime(start);
    }

    private static void logTime(Instant start){
        logger.debug("Elapsed time "+ Duration.between(start, Instant.now()).toMillis()+"ms");
    }
}
