package com.fluxshop.client;

import com.fluxshop.dto.ProductRequest;
import com.fluxshop.entities.Product;
import com.fluxshop.entities.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PerformanceClient {
    private static final String BASE_URL = "http://localhost:8081/";
    private static final String ALL = "ALL";
    private static final String GET = "GET";
    private static final String CREATE = "CREATE";
    private static final String CREATE_MULTIPLE = "CREATE_MULTIPLE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String DELETE_ALL = "DELETE_ALL";
    ProductRepository repository;

    private static WebClient client = WebClient.create(BASE_URL);//"http://localhost:8081?delay=2"

    private void all(){
        client.get().uri("")
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    private void get(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        client.get().uri("" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    private void create(){
        Publisher<Product> products = subscriber -> List.of(new Product("Prod1", "desc1", BigDecimal.valueOf(90.34)),  new ProductRequest("Prod2", "desc2", BigDecimal.valueOf(120.34)), new ProductRequest("Prod3", "desc3", BigDecimal.valueOf(1290.34)));
        client.post().uri("")
                     .bodyValue(new ProductRequest("Prod1", "desc1", BigDecimal.valueOf(90.34))).retrieve()
                     .toBodilessEntity()
                     .block();
    }

    private void update(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        client.put().uri("" + id)
                    .bodyValue(new ProductRequest("Prod1 modified ", "desc1 modified", BigDecimal.valueOf(90.34)))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
    }
    private void delete(){
        Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
       client.delete().uri("" +id)
                      .retrieve()
                      .toBodilessEntity()
                      .block();
    }
    private void deleteAll(){
        client.delete().uri("")
                       .retrieve()
                       .toBodilessEntity()
                       .block();
    }

    void genericFunction( int numberOfThread, int numberOfRequestPerSecond, String url, String CASE) throws  InterruptedException {
        List<Long> times = new ArrayList<>();// times = [];

        for(int i=0; i < numberOfThread; i++){

            Thread t = new Thread(() -> {
                long start, next ,end, elapsed ;
                int step = 1000/numberOfRequestPerSecond; //step = 1000 / rps


                start = System.nanoTime();//start = current time
                next = System.nanoTime(); // next = current time

                while(System.nanoTime() - start < 50000) { //current time - start  < 50000
                    long time = System.nanoTime(); //time = current time
                    if(time < next){ continue; }//if time < next { continue; }
                    //send request
                    for(int j=0; j < step; j++) { // 8ms/re

                        switch (CASE){
                            case ALL -> {   all();     break; }
                            case GET -> {   get();     break; }
                            case CREATE -> { create(); break; }
                            case UPDATE -> { update(); break; }
                            case DELETE -> { delete(); break; }
                            case DELETE_ALL -> { deleteAll(); break; }
                            default -> throw new IllegalStateException("Unexpected value: " );
                        };

                    }
                    end = System.nanoTime(); //end = current time
                    elapsed = end - time;
                    times.add(elapsed); //times.push(elapsed);
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
    }
}
