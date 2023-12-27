# Java Multithreading

### Thread Tests Overview

The `ThreadTests` class contains unit tests for thread-related functionality in Java, showcasing various aspects of
multithreading. </br>
It demonstrates thread creation, exception handling, thread naming, priority configuration, and interruption.

#### Class Structure

The class defines a main test method, `test()`, which creates and manages different threads, including a custom thread
class, `CustomThread`, that extends the Java `Thread` class. The custom thread executes a simple loop and gets
interrupted under a specific condition. Exception handling is also demonstrated for uncaught exceptions.

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

The `SharedObjectTests` class contains tests demonstrating shared state between threads using the `wait` and `notify`
mechanisms in Java. It showcases a scenario where one thread modifies the state of a shared object, and another thread
waits until the state is modified, utilizing synchronization to ensure proper coordination.

### Class Structure

The class defines a test method, `test()`, which creates an instance of `SharedObject` and two threads: a modifying
thread and a checking thread. The modifying thread modifies the state of the shared object and notifies other threads
waiting on it. The checking thread waits until the state is modified and then continues with its logic.

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

The synchronized keyword ensures that changes made by one thread inside a synchronized block are visible to other
threads: </br>
When a thread exits a synchronized block, it releases the lock, and the changes it made are guaranteed to be visible to
other threads, effectively flushing the **_thread's local cache_** to the **_main memory (heap)_**. </br>

### Volatile

For variables declared as volatile, changes made by one thread are immediately visible to other threads, as the local
cache is flushed to main memory. </br>
Volatile applies to individual variables, ensuring visibility for reads and writes, while synchronization (synchronized
blocks/methods) operates on code blocks, ensuring visibility and atomicity for the operations inside the block.

## Locks (LockTests)

The test method demonstrates a simple example of a potential deadlock using ReentrantLocks.

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

Can be considered more versatile and flexible than locks, offering additional synchronization patterns beyond mutual
exclusion (where only one thread or process at a time is granted exclusive access to a particular resource or critical
section of code).

## SingleThreadExecutor (SingleThreadExecutorTests)

The test method showcases the usage of `SingleThreadExecutor` for the sequential execution of tasks.

1. **Create Executor:**
    - Create a `SingleThreadExecutor` to manage the execution of tasks in a single background thread.

2. **Enqueue Tasks:**
    - Enqueue tasks for execution, with each task printing a message indicating its order and the executing thread.
    - Tasks are submitted in a loop, simulating a sequence of asynchronous operations.

3. **Sequential Execution:**
    - `SingleThreadExecutor` ensures that tasks are processed sequentially, adhering to a first-in, first-out (FIFO)
      order.
    - The single background thread is reused for executing all tasks, ensuring sequential execution.

4. **Simulate Processing Time:**
    - Tasks simulate processing time with a sleep, providing a realistic workload for the example.

5. **Join Threads:**
    - Join threads to wait for their completion, allowing the main thread to synchronize with the task execution.

6. **Shutdown Executor:**
    - Shut down the executor to release resources after all tasks are completed.

## FixedThreadPool  (FixedThreadPoolTests)

This method demonstrates the usage of a fixed thread pool with a size of 3.

- Tasks are enqueued for execution in a loop.
- The fixed thread pool ensures that at most 3 tasks are processed concurrently.
- Any additional tasks are queued and executed as threads become available.

## ScheduledExecutorService (ScheduledExecutorServiceTests)

```java
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
```

## CachedThreadPool (CachedThreadPoolTests)

This test class demonstrates the usage of a cached thread pool in Java, specifically using the `Executors.newCachedThreadPool()` factory method. </br> 
The cached thread pool is dynamic, adjusting the number of threads based on the workload. </br>

The test method showcases the basic functionality of a cached thread pool:

- **Thread Pool Creation:** A cached thread pool is created using the `Executors.newCachedThreadPool()` method.

- **Task Submission:** Five tasks are submitted to the thread pool. Each task prints a message indicating its execution, including the task ID and the name of the current thread.

- **Task Simulation:** To simulate some workload, each task includes a sleep period of 2 seconds.

- **ThreadPool Shutdown:** After submitting the tasks, the cached thread pool is shut down when the tasks are completed.
```java
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
```