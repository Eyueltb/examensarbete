package com.fluxshop.tests;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WriteToCSV {

    private static final String RELATIVE_PATH = "performance.csv";

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public void givenDataArray_whenConvertToCSV_thenOutputCreated(RequiredAttribute result) throws IOException, UnsupportedEncodingException {
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]{"Number of records", "Number of threads", "number of request/second", "Action(methods)", "Total time in nano second)", "Project Type" });
        dataLines.add(new String[]{String.valueOf(result.getNumberOfRecords()), String.valueOf(result.getNumberOfThreads()), String.valueOf(result.getNumberOfRequestPerSecond()), result.getAction(), String.valueOf(result.getTimes())});
        File csvOutputFile = new File(RELATIVE_PATH);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
}
