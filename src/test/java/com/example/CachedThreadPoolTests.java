package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class CachedThreadPoolTests {

    /**
     * Test method demonstrating the usage of a cached thread pool. The cached thread pool is dynamic,
     * adjusting the number of threads based on the workload. Tasks are submitted to the pool, and each
     * task prints a message indicating its execution along with the current thread name. The tasks simulate
     * some work by sleeping for 2 seconds. The cached thread pool is then shut down when the tasks are completed.
     */
    @Test
    void test() {
        // Create a cached thread pool
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Submit tasks to the pool
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
                try {
                    // Simulate some task execution time
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor service when done
        executorService.shutdown();
    }

}
