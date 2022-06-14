package com.fluxshop.tests;

import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class TestPerformance {
    private static final String ALL = "ALL";
    private static final String GET = "GET";
    private static final String CREATE = "CREATE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String DELETE_ALL = "DELETE_ALL";
    private static final long  DURATION = 500000000000L; //50 SEC
    private static final int NUMBER_OF_RECORDS = 500;
    private static final String BASE_URL_WEB_FLUX = "http://localhost:8082/api/";
    private static final String BASE_URL_SPRING_BOOT = "http://localhost:8084/api/";
    private static final String WEB_FLUX = "WEB_FLUX";
    private static final String SPRING_BOOT = "SPRING_BOOT";
    private static WebClient client = null;
    ProductApi productApi;
    WriteToCSV writeToCSV;

    private List<Long> myAlgorithm(int numberOfRequestPerSecond, String CASE) {
        List<Long> times = new ArrayList<>();// times = [];
        long start, next ,end, elapsed ;
        // how long it takes between each request(how long it takes between each tick or each request)
        int step = 1000000000 / numberOfRequestPerSecond; //step = 1000 / rps

        start = System.nanoTime();//start = current time
        next = System.nanoTime(); // next = current time

        while(System.nanoTime() - start < DURATION ) { //current time - start  < 50000
            long time = System.nanoTime(); //time = current time
            if(time <  next) { continue; }//if time < next { continue; }
            //send request
            switch (CASE){
                    case ALL -> {           productApi.all();       break; }
                    case GET -> {           productApi.get();       break; }
                    case CREATE -> {        productApi.create();    break; }
                    case UPDATE -> {        productApi.update();    break; }
                    case DELETE -> {        productApi.delete();    break; }
                    case DELETE_ALL -> {    productApi.deleteAll(); break; }
                    default -> throw new IllegalStateException("Unexpected value: " );
            };

            end = System.nanoTime(); //end = current time
            elapsed = end - time;
            times.add(elapsed); //times.push(elapsed);
            next = time + step;
        }
        return times;
    }

    private RequiredAttribute  myFunc(int numberOfThread, int numberOfRequestPerSecond, String CASE, ProjectType type) throws ExecutionException, InterruptedException {

        RequiredAttribute requiredAttribute = new RequiredAttribute(NUMBER_OF_RECORDS,numberOfThread,numberOfRequestPerSecond, CASE, type);
        CompletableFuture<List<Long>> completableFuture = null;

        for(int i=0; i < numberOfThread; i++) {
            completableFuture = CompletableFuture.supplyAsync(() -> myAlgorithm(numberOfRequestPerSecond, CASE));
        }
        while (!completableFuture.isDone()) {
            System.out.println("CompletableFuture is not finished yet...");
        }
        final List<Long> longs = completableFuture.get();
        requiredAttribute.addAll(longs);
        return requiredAttribute;
    }

    public void test_me() throws IOException, ExecutionException, InterruptedException {
        test_me_again( 4, 10, "ALL" ,"WEB_FLUX");
    }

    private static WebClient getClient(String BASE_URL){

        return switch (BASE_URL){
            case SPRING_BOOT -> client = WebClient.create(BASE_URL_SPRING_BOOT);
            case WEB_FLUX -> client = WebClient.create(BASE_URL_WEB_FLUX);
            default -> throw new IllegalStateException("Unexpected value: " );
        };
    }

    private void test_me_again( int numberOfThread, int numberOfRequestPerSecond,String CASE,String projectType) throws ExecutionException, InterruptedException, IOException {
        switch (projectType) {

            case WEB_FLUX -> {
                getClient(WEB_FLUX);
                for (int i = 0; i < 10; i++) {
                    RequiredAttribute results = myFunc(numberOfThread, numberOfRequestPerSecond, CASE, ProjectType.WEB_FLUX);
                    writeToCSV.givenDataArray_whenConvertToCSV_thenOutputCreated(results);
                }
                break;
            }
            case SPRING_BOOT -> {
                getClient(SPRING_BOOT);
                for (int i = 0; i < 10; i++) {
                    RequiredAttribute results = myFunc(numberOfThread, numberOfRequestPerSecond, CASE, ProjectType.SPRING_BOOT);
                    writeToCSV.givenDataArray_whenConvertToCSV_thenOutputCreated(results);
                }
               break;
            }
        }
    }

}
