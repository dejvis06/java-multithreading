package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CompletableFutureTests {

    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureTests.class);

    @Test
    void composition() {
        // Task 1: Asynchronous computation that returns a string after a delay
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000); // Simulate some asynchronous computation
            } catch (InterruptedException e) {
                logger.error("Task 1 encountered an error", e);
            }
            return "Hello";
        });

        // Task 2: Asynchronous computation that returns a string after a delay
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // Simulate some asynchronous computation
            } catch (InterruptedException e) {
                logger.error("Task 2 encountered an error", e);
            }
            return "World";
        });

        // Combine the results of the two tasks when both are complete
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (result1, result2) -> result1 + " " + result2);

        // Block and get the result when both tasks are complete
        try {
            String result = combinedFuture.get();
            logger.info("Combined result: {}", result); // Use logger for output
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred while processing tasks", e);
        }
    }

    @Test
    void exceptionHandling() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("Error occurred");
            }
            return "Hello";
        });

        future.exceptionally(ex -> {
            logger.error("Exception occurred: {}", ex.getMessage());
            return "Default Value";
        });

        future.thenAccept(result -> logger.info("Result: {}", result));
    }

    @Test
    void chaining() {
        CompletableFuture<String> initialFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> chainedFuture = initialFuture.thenApplyAsync(result -> result + " World");
        chainedFuture.thenAccept(result -> logger.info("Chained Result: {}", result));
    }

    @Test
    void completionCallbacks() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

        future.thenAccept(result -> logger.info("Success: {}", result));

        future.exceptionally(ex -> {
            logger.error("Exception occurred: {}", ex.getMessage());
            return "Default Value";
        });
    }

    @Test
    void timeoutsAndErrorHandling() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        });

        CompletableFuture<String> resultFuture = future.completeOnTimeout("Default Value", 2000, TimeUnit.MILLISECONDS);
        resultFuture.thenAccept(result -> logger.info("Result: {}", result));
        resultFuture.get();
    }
}
