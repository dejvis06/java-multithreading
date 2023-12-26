package com.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests demonstrating shared state between threads using wait and notify.
 */
@SpringBootTest
public class SharedObjectTests {

    private final Logger logger = LoggerFactory.getLogger(SharedObjectTests.class);

    @Test
    void test() {
        SharedObject sharedObject = new SharedObject();

        Thread modifyingThread = new Thread(() -> {
            logger.info("Modifying thread is starting...");

            // Modify the state
            sharedObject.state = true;
            logger.info("State modified to true");

            // Notify other threads waiting on the sharedObject
            synchronized (sharedObject) {
                logger.info("Sending notification sent to waiting threads");
                sharedObject.notify();
            }

            logger.info("Modifying thread is finishing...");
        });

        Thread checkingThread = new Thread(() -> {
            logger.info("Checking thread is starting...");

            // Wait until the state is modified
            synchronized (sharedObject) {
                while (!sharedObject.state) {
                    try {
                        logger.info("Waiting for state modification...");
                        sharedObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("State has been modified. Continuing with other logic.");
            }

            logger.info("Checking thread is finishing...");
        });

        checkingThread.start();
        modifyingThread.start();
    }

    private class SharedObject {
        boolean state = false;
    }
}
