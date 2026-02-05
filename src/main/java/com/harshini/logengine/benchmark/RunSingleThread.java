package com.harshini.logengine.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.harshini.logengine.engine.AggregationResult;
import com.harshini.logengine.engine.SingleThreadAnalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunSingleThread {

    public static void main(String[] args) throws Exception {
        Path input = Paths.get("sample-logs/big.log");
        Files.createDirectories(Paths.get("output"));


        long start = System.nanoTime();
        AggregationResult result = SingleThreadAnalyzer.analyze(input);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get("output/summary-single.json").toFile(), result);

        System.out.println("Single-thread done.");
        System.out.println("Lines: " + result.getTotalLines() + ", malformed: " + result.getMalformedLines());
        System.out.println("Time: " + elapsedMs + " ms");
    }
}