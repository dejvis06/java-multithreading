# Java Multithreading

### Thread Tests Overview
The `ThreadTests` class contains unit tests for thread-related functionality in Java, showcasing various aspects of multithreading. </br>
It demonstrates thread creation, exception handling, thread naming, priority configuration, and interruption.

#### Class Structure
The class defines a main test method, `test()`, which creates and manages different threads, including a custom thread class, `CustomThread`, that extends the Java `Thread` class. The custom thread executes a simple loop and gets interrupted under a specific condition. Exception handling is also demonstrated for uncaught exceptions.

#### Test Method: test()
```java
@SpringBootTest
class ThreadTests {

    private final Logger logger = LoggerFactory.getLogger(ThreadTests.class);

    @Test
    void test() {
        // Main thread naming
        Thread.currentThread().setName("main-thread");

        // Creating and configuring a worker thread
        Thread thread = new Thread(() -> {
            logger.info("Created thread name: {}", Thread.currentThread().getName());
            throw new RuntimeException("Test exception handler");
        });

        // Main thread information
        logger.info("Main thread name: {}", Thread.currentThread().getName());

        // Configuring and starting the worker thread
        thread.setName("worker-thread");
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            logger.error("Caught exception: \"{}\" ,in thread: {}", e.getMessage(), t);
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        // Starting a custom thread
        new CustomThread("my-custom-thread").start();
    }

    // CustomThread class definition
    private class CustomThread extends Thread {

        public CustomThread(String name) {
            this.setName(name);
            this.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
                logger.error("Caught exception: \"{}\" ,in thread: {}", e.getMessage(), t);
            });
        }

        @Override
        public void run() {
            // Custom thread execution
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
```
#### Usage

- The `test()` method demonstrates the creation and management of threads.
- The main thread is named "main-thread," and a worker thread is created with the name "worker-thread."
- The worker thread is configured with a custom uncaught exception handler, maximum priority, and is started.
- Additionally, a custom thread (`CustomThread`) is created with its own exception handler and is started.


## Shared Object Tests Overview
The `SharedObjectTests` class contains tests demonstrating shared state between threads using the `wait` and `notify` mechanisms in Java. It showcases a scenario where one thread modifies the state of a shared object, and another thread waits until the state is modified, utilizing synchronization to ensure proper coordination.

### Class Structure
The class defines a test method, `test()`, which creates an instance of `SharedObject` and two threads: a modifying thread and a checking thread. The modifying thread modifies the state of the shared object and notifies other threads waiting on it. The checking thread waits until the state is modified and then continues with its logic.

#### Test Method: test()
```java
@SpringBootTest
public class SharedObjectTests {

    private final Logger logger = LoggerFactory.getLogger(SharedObjectTests.class);

    @Test
    void test() {
        SharedObject sharedObject = new SharedObject();

        // Modifying thread
        Thread modifyingThread = new Thread(() -> {
            logger.info("Modifying thread is starting...");

            // Modify the state
            sharedObject.state = true;
            logger.info("State modified to true");

            // Notify other threads waiting on the sharedObject
            synchronized (sharedObject) {
                logger.info("Sending notification to waiting threads");
                sharedObject.notify();
            }

            logger.info("Modifying thread is finishing...");
        });

        // Checking thread
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

        // Start both threads
        checkingThread.start();
        modifyingThread.start();
    }

    // SharedObject class definition
    private class SharedObject {
        boolean state = false;
    }
}
```

#### Usage

- The `test()` method demonstrates the coordination between two threads using the `wait` and `notify` mechanisms.
- A shared object, `SharedObject`, is created with a boolean state initially set to `false`.
- The modifying thread modifies the state to `true` and notifies the waiting threads.
- The checking thread waits until the state is modified, using synchronization to ensure proper coordination.

The synchronized keyword ensures that changes made by one thread inside a synchronized block are visible to other threads: </br>
When a thread exits a synchronized block, it releases the lock, and the changes it made are guaranteed to be visible to other threads, effectively flushing the **_thread's local cache_** to the **_main memory (heap)_**. </br>

### Volatile
For variables declared as volatile, changes made by one thread are immediately visible to other threads, as the local cache is flushed to main memory. </br>
Volatile applies to individual variables, ensuring visibility for reads and writes, while synchronization (synchronized blocks/methods) operates on code blocks, ensuring visibility and atomicity for the operations inside the block.

## Locks (LockTests)

This test method demonstrates a simple example of a potential deadlock using ReentrantLocks.

### Thread Actions

#### Thread 1:
- Acquires lock1.
- Sleeps for 100 milliseconds (simulating some work).
- Releases lock1.
- Waits for lock2.
- Acquires lock2.

#### Thread 2:
- Acquires lock2.
- Sleeps for 500 milliseconds (introducing a delay to increase the chance of deadlock).
- Releases lock2.
- Waits for lock1.
- Acquires lock1.

## Semaphores

Can be considered more versatile and flexible than locks, offering additional synchronization patterns beyond mutual exclusion (where only one thread or process at a time is granted exclusive access to a particular resource or critical section of code).

