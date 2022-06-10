package com.fluxshop.tests;

import lombok.AllArgsConstructor;

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
    private static final String CREATE_MULTIPLE = "CREATE_MULTIPLE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String DELETE_ALL = "DELETE_ALL";
    private static final long  DURATION = 500000000000L; //50 SEC
    private static final int NUMBER_OF_RECORDS = 500;

    ProductApi productApi;
    WriteToCSV writeToCSV;
    private List<Long> myAlgorithm(int numberOfRequestPerSecond, String CASE) {
        List<Long> times = new ArrayList<>();// times = [];
        long start, next ,end, elapsed ;
        // how long it takes between each request(how long it takes between each tick or each request)
        int step = 1000 / numberOfRequestPerSecond; //step = 1000 / rps

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

    private RequiredAttribute  myFunc(int numberOfThread, int numberOfRequestPerSecond, String CASE) throws ExecutionException, InterruptedException {
        RequiredAttribute requiredAttribute = new RequiredAttribute(NUMBER_OF_RECORDS,numberOfThread,numberOfRequestPerSecond, CASE);
        CompletableFuture<List<Long>> completableFuture = null;
        for(int i=0; i < numberOfThread; i++) {
            completableFuture = CompletableFuture.supplyAsync(() -> myAlgorithm(numberOfRequestPerSecond, CASE));
        }
        while (!completableFuture.isDone()) {
            System.out.println("CompletableFuture is not finished yet...");
        }
        final List<Long> longs = completableFuture.get();
        requiredAttribute.addAll(longs);

        System.out.println("size" + longs.size());

        System.out.println(" times ");
        longs.stream().forEach(System.out::print);
        //return longs;
        return requiredAttribute;
    }

    public void test_me() throws IOException, ExecutionException, InterruptedException {
        //myFunc(10, 1000, "ALL");
        RequiredAttribute results =  myFunc(10, 1000, "ALL");;
        writeToCSV.givenDataArray_whenConvertToCSV_thenOutputCreated(results);
    }

}
