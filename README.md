# Java Multithreading
This README.md file provides an overview of the classes in the test package.

## ThreadTests

### Overview
The ThreadTests class contains unit tests for thread-related functionality, showcasing various aspects of Java multithreading. It covers thread creation, exception handling, thread naming, priority configuration, and interruption.

### Main Test Method
The primary test method is contextLoads(), which performs the following tasks:

- Sets the name of the main thread to "main-thread."
- Creates a worker thread that logs its name and intentionally throws a runtime exception to demonstrate exception handling.
- Configures the worker thread with a custom uncaught exception handler.
- Sets the priority of the worker thread to Thread.MAX_PRIORITY.
- Starts the worker thread.
- Initiates a custom thread (CustomThread) named "my-custom-thread" that runs a simple loop and gets interrupted under a specific condition.
- Demonstrates exception handling for uncaught exceptions in the custom thread.

- **CustomThread:**
  The `CustomThread` class extends the Java `Thread` class and includes the following features:
    - Custom constructor setting the thread name and configuring the uncaught exception handler.
    - Run method implementing a loop that logs thread execution and interrupts the thread under a specific condition.
    - Option to make the custom thread a daemon thread (commented out in the code).



## SharedObjectTests
### Overview
The SharedObjectTests class demonstrates shared state between threads using the wait and notify mechanisms.

### Main Test Method
The contextLoads() method of SharedObjectTests performs the following:

- Creates an instance of the `SharedObject` class.
- Initializes two threads:
    - **Modifying thread:** Modifies the state of the shared object, notifies waiting threads, and logs its activities.
    - **Checking thread:** Waits until the state is modified, then logs its activities.
- Starts both threads.

### SharedObject
The `SharedObject` class is a simple container with a boolean state variable, initially set to `false`.
