package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

/**
 * This class contains unit tests for thread-related functionality, showcasing various aspects
 * of Java multithreading. It demonstrates thread creation, exception handling, thread naming,
 * priority configuration, and interruption.
 * <p>
 * The class defines a main test method, {@code contextLoads()}, which creates and manages
 * different threads, including a custom thread class, {@code CustomThread}, that extends the
 * Java {@code Thread} class. The custom thread executes a simple loop and gets interrupted
 * under a specific condition. Exception handling is also demonstrated for uncaught exceptions.
 *
 * @see Thread
 * @see UncaughtExceptionHandler
 * @see ThreadGroup
 * @see Random
 * @see Logger
 * @see LoggerFactory
 * @see Test
 * @see SpringBootTest
 */
@SpringBootTest
class ThreadTests {

    private final Logger logger = LoggerFactory.getLogger(ThreadTests.class);

    @Test
    void test() {
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
            // main thread doesnt wait for this thread to finish -> this.setDaemon(true);
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
