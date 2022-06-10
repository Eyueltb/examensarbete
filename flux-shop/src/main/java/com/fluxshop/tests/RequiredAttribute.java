package com.fluxshop.tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequiredAttribute {

    private int numberOfRecords;
    private int numberOfThreads;
    private int numberOfRequestPerSecond;
    private String action;
    private List<Long> times;

    public RequiredAttribute(int numberOfRecords, int numberOfThreads, int numberOfRequestPerSecond, String action){
        this.numberOfRecords = numberOfRecords;
        this.numberOfThreads = numberOfThreads;
        this.numberOfRequestPerSecond = numberOfRequestPerSecond;
        this.action = action;
        this.times = new ArrayList<>();
    }

    public void add(Long time){
        this.times.add(time);
    }
    public void addAll(List<Long> times){
        this.times = times;
    }
}
