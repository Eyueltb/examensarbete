package com.fluxshop;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestPerformance {

    private static final String BASE_URL_WEB_FLUX = "http://localhost:8082/api/";
    private static final String BASE_URL_SPRING_MVC = "http://localhost:8084/api/";
    private static final String WEB_FLUX = "WEB_FLUX";
    private static final String SPRING_MVC = "SPRING_MVC";
    private static final String ALL = "ALL";
    private static final String GET = "GET";
    private static final String CREATE = "CREATE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String DELETE_ALL = "DELETE_ALL";
    private static final long  DURATION = 500000000000L; //50 SEC
    private static final String RELATIVE_PATH = "c:\\resources\\performance.csv";
    private final String ABSOLUTE_PATH = "C:/Users/eyuel/Documents/project/java/IdeaProjects/Thesis/examensarbete/flux-shop/src/main/resources/p1.csv";

    private static WebClient client = null;
    private static final int NUMBER_OF_RECORDS = 500;

    private static WebClient getClient(String BASE_URL){

        return switch (BASE_URL){
            case SPRING_MVC -> client = WebClient.create(BASE_URL_SPRING_MVC);
            case WEB_FLUX -> client = WebClient.create(BASE_URL_WEB_FLUX);
            default -> throw new IllegalStateException("Unexpected value: " );
        };
    }

    private void all(){
        client.get().uri("")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void get(){
        String id = "24307ba2-105b-4070-901c-b68334e5f76f";
        client.get().uri("" + id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    private void create(){
        //Publisher<Product> products = subscriber -> List.of(new Product("Prod1", "desc1", BigDecimal.valueOf(90.34)),  new ProductRequest("Prod2", "desc2", BigDecimal.valueOf(120.34)), new ProductRequest("Prod3", "desc3", BigDecimal.valueOf(1290.34)));
        client.post().uri("")
                .bodyValue(new ProductRequest("Product New", "desc NEW ", BigDecimal.valueOf(90.34))).retrieve()
                .toBodilessEntity()
                .block();
    }

    private void update(){
        //Mono<String> id = repository.findAll().singleOrEmpty().map(Product::getId);
        String id = "24307ba2-105b-4070-901c-b68334e5f76f";
        client.put().uri("" + id)
                .bodyValue(new ProductRequest("Prod48 modified ", "desc48 modified", BigDecimal.valueOf(90.34)))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    private void delete(){
       String id = "a51130bd-baf4-424a-a444-69309a6ef4d3";
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

    private List<Long> unprovedAlgorithm(int numberOfRequestPerSecond, String CASE) { //myAlgorithm(int numberOfRequestPerSecond, Consumer<String> lambda)
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
             switch (CASE){ //lambda.accept(CASE)
                    case ALL -> {   all();     break; }
                    case GET -> {   get();     break; }
                    case CREATE -> { create(); break; }
                    case UPDATE -> { update(); break; }
                    case DELETE -> { delete(); break; }
                    case DELETE_ALL -> { deleteAll(); break; }
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
            completableFuture = CompletableFuture.supplyAsync(() -> unprovedAlgorithm(numberOfRequestPerSecond, CASE));
        }
        while (!completableFuture.isDone()) {
            System.out.println("CompletableFuture is not finished yet...");
        }
        final List<Long> longs = completableFuture.get();
        requiredAttribute.addAll(longs);
        return requiredAttribute;
    }



    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }
    @Test

     public void test_me() throws IOException, ExecutionException, InterruptedException {
        test_me_again( 4, 10, "ALL" ,"WEB_FLUX");
    }

    private void test_me_again( int numberOfThread, int numberOfRequestPerSecond,String CASE,String projectType) throws ExecutionException, InterruptedException, IOException {

        switch (projectType){

            case WEB_FLUX -> {
                /*getClient(WEB_FLUX);
                for (int i = 0; i < 10; i++) {
                    RequiredAttribute results = myFunc(numberOfThread, numberOfRequestPerSecond, CASE, ProjectType.WEB_FLUX);
                    givenDataArray_whenConvertToCSV_thenOutputCreated(results);
                }
                break;*/
                long [] times = new long[10];
                getClient(WEB_FLUX);
                RequiredAttribute results = null;
                for (int i = 0; i < 10; i++) {
                    results = myFunc(numberOfThread, numberOfRequestPerSecond, CASE, ProjectType.WEB_FLUX);
                    times[i]= results.getTimes().get(0);
                }
                long sum = Arrays.stream(times).sum();
                write_to_csv(results, sum/times.length);
                break;
            }
            case SPRING_MVC -> {
                long [] times = new long[10];
                getClient(SPRING_MVC);
                RequiredAttribute results = null;
                for (int i = 0; i < 10; i++) {
                    results = myFunc(numberOfThread, numberOfRequestPerSecond, CASE, ProjectType.SPRING_MVC);
                    times[i]= results.getTimes().get(0);
                }
                long sum = Arrays.stream(times).sum();
                write_to_csv(results, sum/10);
                break;
            }
        }
    }

    private void write_to_csv(RequiredAttribute result, Long count) throws IOException {
        List<String[]> dataLines = new ArrayList<>();
        //dataLines.add(new String[]{"Number of records", "Number of threads", "number of request/second", "Action(methods)", "Total time in nano second)", "ProjectType" });
        dataLines.add(new String[]{String.valueOf(result.getNumberOfRecords()), String.valueOf(result.getNumberOfThreads()), String.valueOf(result.getNumberOfRequestPerSecond()), result.getAction(), String.valueOf(count), String.valueOf(result.getType())});
        File csvOutputFile = new File(ABSOLUTE_PATH);
        try (BufferedWriter pw = new BufferedWriter(new FileWriter(csvOutputFile, true));) { //PrintWriter pw = new PrintWriter(csvOutputFile)
            //.forEach(pw::println);
            pw.newLine();
            for (String[] dataLine : dataLines) {
                String s = convertToCSV(dataLine);
                pw.write(s);
            }
        }
    }
    public void givenDataArray_whenConvertToCSV_thenOutputCreated(RequiredAttribute result) throws IOException {
        List<String[]> dataLines = new ArrayList<>();
        //dataLines.add(new String[]{"Number of records", "Number of threads", "number of request/second", "Action(methods)", "Total time in nano second)", "ProjectType" });
        dataLines.add(new String[]{String.valueOf(result.getNumberOfRecords()), String.valueOf(result.getNumberOfThreads()), String.valueOf(result.getNumberOfRequestPerSecond()), result.getAction(), String.valueOf(result.getTimes().get(0)), String.valueOf(result.getType())});
        File csvOutputFile = new File(ABSOLUTE_PATH);
        try (BufferedWriter pw = new BufferedWriter(new FileWriter(csvOutputFile, true));) { //PrintWriter pw = new PrintWriter(csvOutputFile)
            //.forEach(pw::println);
            pw.newLine();
            for (String[] dataLine : dataLines) {
                String s = convertToCSV(dataLine);
                pw.write(s);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RequiredAttribute {
        private int numberOfRecords;
        private int numberOfThreads;
        private int numberOfRequestPerSecond;
        private String action;
        private List<Long> times;
        private ProjectType type;

        public RequiredAttribute(int numberOfRecords, int numberOfThreads, int numberOfRequestPerSecond, String action, ProjectType type){
            this.numberOfRecords = numberOfRecords;
            this.numberOfThreads = numberOfThreads;
            this.numberOfRequestPerSecond = numberOfRequestPerSecond;
            this.action = action;
            this.type = type;
            this.times = new ArrayList<>();
        }

        public void add(Long time){
            this.times.add(time);
        }
        public void addAll(List<Long> times){
            this.times = times;
        }
    }


    public enum ProjectType {
        WEB_FLUX, SPRING_MVC
    }
}
