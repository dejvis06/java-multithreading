package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class JavaMultithreadingApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(JavaMultithreadingApplicationTests.class);

    @Test
    void contextLoads() {
        Thread.currentThread().setName("main-thread");

        Thread thread = new Thread(() -> {
            logger.info("Created thread name: {}", Thread.currentThread().getName());
            throw new RuntimeException("Test exception handler");
        });

        logger.info("Main thread name: {}", Thread.currentThread().getName());
        thread.setName("worker-thread");
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            logger.error("Caught exception: \"{}\" ,in thread: {}", e.getMessage(), t);
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        new CustomThread("my-custom-thread").start();
    }

    private class CustomThread extends Thread {

        public CustomThread(String name) {
            this.setName(name);
            this.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
                logger.error("Caught exception: \"{}\" ,in thread: {}", e.getMessage(), t);
            });
        }

        @Override
        public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Executing thread: {}", this.getName());
                int randomNo = new Random().nextInt(6);
                if (randomNo == 1) {
                    Thread.currentThread().interrupt();
                }
            }
            logger.info("Thread: {} got interrupted", this.getName());
        }
    }

}
