package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class SingleThreadExecutorTests {

    private static final Logger logger = LoggerFactory.getLogger(SingleThreadExecutorTests.class);

    /**
     * This test method demonstrates the use of SingleThreadExecutor for sequential execution of tasks.
     * The SingleThreadExecutor is created, and tasks are enqueued for execution.
     * The SingleThreadExecutor ensures that tasks are processed sequentially, in a first-in, first-out (FIFO) order.
     * The method then shuts down the executor to release resources.
     */
    @Test
    void test() throws InterruptedException {
        // Create a SingleThreadExecutor
        TaskQueue taskQueue = new TaskQueue(Executors.newSingleThreadExecutor());

        // Enqueue tasks
        for (int i = 1; i <= 5; i++) {
            final int taskNumber = i;
            taskQueue.enqueue(() -> {
                logger.info("Task {} is being processed by {}", taskNumber, Thread.currentThread().getName());
                // Simulate some processing time
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Task {} sleep interrupted", taskNumber, e);
                }
            });
        }

        // Shutdown the executor to release resources
        taskQueue.shutdown();
    }

    class TaskQueue {
        private final ExecutorService executor;

        public TaskQueue(ExecutorService executor) {
            this.executor = executor;
        }

        public void enqueue(Runnable task) {
            executor.submit(task);
        }

        public void shutdown() throws InterruptedException {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        }
    }
}