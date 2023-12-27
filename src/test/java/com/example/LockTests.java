package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
public class LockTests {

    private static final Logger logger = LoggerFactory.getLogger(LockTests.class);
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    /**
     * This test method creates two threads that acquire locks in different order
     *
     * Thread 1:
     *  - Acquires lock1.
     *  - Sleeps for 100 milliseconds (simulating some work).
     *  - Releases lock1.
     *  - Waits for lock2.
     *  - Acquires lock2.
     *
     * Thread 2:
     *  - Acquires lock2.
     *  - Sleeps for 500 milliseconds (introducing a delay to increase the chance of deadlock).
     *  - Releases lock2.
     *  - Waits for lock1.
     *  - Acquires lock1.
     *
     * Both threads attempt to acquire the other lock while holding one lock,
     * potentially leading to a deadlock if the timing is unfortunate.
     */
    @Test
    void test() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            lock1.lock();
            logger.info("Thread 1: Holding lock 1...");

            try {
                Thread.sleep(100); // Introducing a delay to increase the chance of deadlock
            } catch (InterruptedException e) {
                logger.error("Thread 1 sleep interrupted", e);
            }

            lock1.unlock();
            logger.info("Thread 1: Waiting for lock 2...");
            lock2.lock();
            logger.info("Thread 1: Holding lock 1 and lock 2...");
            lock2.unlock();

        });

        Thread thread2 = new Thread(() -> {
            lock2.lock();
            logger.info("Thread 2: Holding lock 2...");

            try {
                Thread.sleep(500); // Introducing a delay to increase the chance of deadlock
            } catch (InterruptedException e) {
                logger.error("Thread 2 sleep interrupted", e);
            }

            lock2.unlock();
            logger.info("Thread 2: Waiting for lock 1...");
            lock1.lock();
            logger.info("Thread 2: Holding lock 2 and lock 1...");
            lock1.unlock();

        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
