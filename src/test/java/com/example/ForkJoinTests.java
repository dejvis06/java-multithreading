package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@SpringBootTest
public class ForkJoinTests {

    private static final Logger logger = LoggerFactory.getLogger(ForkJoinTests.class);

    @Test
    void test() {
        int arraySize = 10000;
        long[] array = new long[arraySize];

        // Initialize the array with some values
        for (int i = 0; i < arraySize; i++) {
            array[i] = i + 1;
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinSum task = new ForkJoinSum(array, 0, array.length);

        long result = forkJoinPool.invoke(task);

        // Use SLF4J logger instead of System.out.println
        logger.info("Sum: {}", result);
    }

    class ForkJoinSum extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1000; // Threshold for task granularity
        private long[] array;
        private int start;
        private int end;

        ForkJoinSum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            // If the task is small enough, perform the computation directly
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // Otherwise, split the task into two subtasks
                int mid = (start + end) / 2;
                ForkJoinSum leftTask = new ForkJoinSum(array, start, mid);
                ForkJoinSum rightTask = new ForkJoinSum(array, mid, end);

                // Fork the subtasks in parallel
                leftTask.fork();
                rightTask.fork();

                // Combine the results of the subtasks
                return leftTask.join() + rightTask.join();
            }
        }
    }
}