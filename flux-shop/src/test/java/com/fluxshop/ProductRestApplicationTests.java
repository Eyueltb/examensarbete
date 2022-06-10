package com.fluxshop;

import com.fluxshop.entities.Product;
import com.fluxshop.entities.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductRestApplicationTests {
    //ExecutorService executorService = Executors..newSingleThreadExecutor();
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
    void tearDown() { productRepository.deleteAll().block(); }
    @Test
    void test_all_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3)).collectList().block();

        // When
        List<ProductApi.Product> products = productApi.all().collectList().block();

        // Then
        assertEquals(List.of("prod1", "prod2", "prod3"), products.stream().map(ProductApi.Product::getName).collect(Collectors.toList()));
    }
    @Test
    void test_get_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3)).collectList().block();

        // When
        ProductApi.Product product = productApi.get(prod1.getId()).block();

        // Then
        assertEquals("prod1", product.getName());
    }

    @Test
    void test_create_success() {
        // When
        ProductApi.Product product = productApi.create("prod4", "description4", BigDecimal.valueOf(45)).block();

        // Then
        Product verifyProduct = productRepository.findAll().single().block();
        assertEquals(verifyProduct.getName(), product.getName());
        assertEquals(verifyProduct.getDescription(), product.getDescription());
        assertEquals(verifyProduct.getPrice(), product.getPrice());
        assertEquals(verifyProduct.getId(), product.getId());
    }

    @Test
    void test_update_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3)).collectList().block();

        // When
        ProductApi.Product product = productApi.update(prod1.getId(), "prod1 modified","description1 modified", BigDecimal.valueOf(234.987) ).block();

        // Then
        Product verifyGroup = productRepository.findById(prod1.getId()).block();
        assertEquals("prod1 modified", product.getName());
        assertEquals("prod1 modified", verifyGroup.getName());
    }

    @Test
    void test_delete_success() {
        // Given
        productRepository.saveAll(List.of(prod1, prod2, prod3)).collectList().block();

        // When
        productApi.delete(prod1.getId()).block();

        // Then
        List<Product> verifyProduct = productRepository.findAll().collectList().block();
        assertEquals(List.of("prod1", "prod2"), verifyProduct.stream().map(Product::getName).collect(Collectors.toList()));
    }
  //1. What happens for 1 thread, multiple th

    void genericFunction( int numberOfThread, int numberOfRequestPerSecond, String url) throws  InterruptedException {

        List<Long> times = new ArrayList<>();// times = [];
        for(int i=0; i < numberOfThread;i++){

            Thread t = new Thread(() -> {
               long start, next ,end, elapsed = 0;
               int step = 1000/numberOfRequestPerSecond; //step = 1000 / rps


                start = System.nanoTime();//start = current time
                next= System.nanoTime(); // next = current time

                while(System.nanoTime() - start < 50000) { //current time - start  < 50000
                   long time = System.nanoTime(); //time = current time
                    if(time < next){ continue; }//if time < next { continue; }
                    //send request
                    for(int j=0; j < step; j++) { // 8ms/re

                     client.get().uri(url)
                                 .retrieve()
                                .toBodilessEntity()
                                .block();
                    }
                    end = System.nanoTime();//end = current time
                    elapsed = end - time;
                    times.add(elapsed);//times.push(elapsed);
                    next = time + step;
                    //save in the file
                }
            });
            
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //save to file here
        System.out.println(times);

    }

    @Test
    void testME() throws InterruptedException {
        genericFunction(1,5, "/api/products");
    }
    private void writeToFile(List<Long> times) throws IOException {
        String fileName ="";
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(times);
        printWriter.close();
    }
    @Test
    void test_performance_webClient_all_success() {
        Instant start = Instant.now();
        System.out.println("hey");
        for(int i=0; i < 100; i++) { // 8ms/re
            //save time in the file, one thread

            client.get().uri("/api/products")
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
      /*  Flux.range(1,20)

                .flatMap(i->  client.get().uri("/api/products")
                        .exchangeToFlux(clientResponse -> {
                            clientResponse.statusCode();
                            clientResponse.headers();
                            clientResponse.toEntity(ProductResponse.class);
                            return clientResponse.bodyToFlux(ProductResponse.class);
                        }));*/

        logTime(start);
    }

    @Test
    void test_performance_all_success() {
        Instant start = Instant.now();
// Given
        productRepository.saveAll(List.of(prod1, prod2, prod3)).collectList().block();

        // When
        List<ProductApi.Product> products = productApi.all().collectList().block();

        // Then
        assertEquals(List.of("prod1", "prod2", "prod3"), products.stream().map(ProductApi.Product::getName).collect(Collectors.toList()));

        logTime(start);
    }

    private static void logTime(Instant start){
        System.out.println("Elapsed time "+ Duration.between(start, Instant.now()).toMillis()+"ms");
    }
}
