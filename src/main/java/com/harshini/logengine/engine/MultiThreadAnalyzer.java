package com.harshini.logengine.engine;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadAnalyzer {

    public static AggregationResult analyze(Path logFile, int threads, int batchSize) throws Exception {
        List<List<String>> batches = BatchReader.readBatches(logFile, batchSize);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<AggregationResult>> futures = new java.util.ArrayList<>(batches.size());

            for (List<String> batch : batches) {
                futures.add(pool.submit(new BatchTask(batch)));
            }

            AggregationResult finalResult = new AggregationResult();
            for (Future<AggregationResult> f : futures) {
                finalResult.merge(f.get());
            }

            return finalResult;
        } finally {
            pool.shutdown();
        }
    }
}