package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class FixedThreadPoolTests {

    private static final Logger logger = LoggerFactory.getLogger(FixedThreadPoolTests.class);

    /**
     * This method demonstrates the usage of a fixed thread pool with a size of 3.
     * Tasks are enqueued for execution in a loop
     * The fixed thread pool ensures that at most 3 tasks are processed concurrently, and any additional
     * tasks are queued and executed as threads become available.
     */
    @Test
    void test() {
        // Create a fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit tasks to the fixed thread pool
        for (int i = 1; i <= 5; i++) {
            final int taskNumber = i;
            executor.submit(() -> {
                logger.info("Task {} executed by {}", taskNumber, Thread.currentThread().getName());
            });
        }

        // Shut down the executor to release resources
        executor.shutdown();
    }
}
