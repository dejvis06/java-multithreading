package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ScheduledExecutorServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorServiceTests.class);

    /**
     * Test method demonstrating the usage of a ScheduledExecutorService with two scheduled tasks.
     * Task 1 runs at a fixed rate of every 2 seconds, and Task 2 runs with a fixed delay of 3 seconds
     * after the completion of each execution. The tasks log their execution times using SLF4J.
     * The test allows the scheduled tasks to run for 15 seconds before completion, providing
     * sufficient time for execution.
     */
    @Test
    void test() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            logger.info("Task 1 is running at: {}", System.currentTimeMillis());
        }, 0, 2, TimeUnit.SECONDS);

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            logger.info("Task 2 is running at: {}", System.currentTimeMillis());
        }, 5, 3, TimeUnit.SECONDS);

        // Allow the scheduled tasks to run for a sufficient duration
        try {
            Thread.sleep(7000); // Sleep for 15 seconds or adjust as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
