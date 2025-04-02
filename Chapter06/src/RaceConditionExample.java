/*
 * This program demonstrates a race condition in Java and provides options to observe it or fix it.
 * Multiple threads increment a shared counter variable concurrently. Without synchronization,
 * the final value is unpredictable due to race conditions. With synchronization, the result
 * is consistent and correct.
 */
public class RaceConditionExample {
    // Shared counter variable accessed by multiple threads
    private static int counter = 0;
    
    // Number of threads to create
    private static final int NUM_THREADS = 4;
    
    // Number of increments per thread
    private static final int INCREMENTS_PER_THREAD = 100000;
    
    // Flag to enable/disable synchronization (set to false to see race condition)
    private static final boolean USE_SYNCHRONIZATION = false;
    
    // Object used as a lock for synchronization
    private static final Object lock = new Object();

    // Worker thread class that increments the counter
    static class CounterThread extends Thread {
        private final String name;

        public CounterThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            // Each thread increments the counter a specified number of times
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
                if (USE_SYNCHRONIZATION) {
                    // Synchronized block to prevent race condition
                    synchronized (lock) {
                        counter++; // Critical section: increment counter safely
                    }
                } else {
                    // No synchronization: race condition occurs here
                    counter++; // Critical section: unprotected increment
                }
            }
            // Print completion message for this thread
            System.out.println(name + " completed incrementing.");
        }
    }

    public static void main(String[] args) {
        // Array to hold all threads
        Thread[] threads = new Thread[NUM_THREADS];

        // Create and start multiple threads
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new CounterThread("Thread-" + (i + 1));
            threads[i].start();
            System.out.println("Started " + threads[i].getName());
        }

        // Wait for all threads to finish using join() instead of sleep()
        for (Thread thread : threads) {
            try {
                thread.join(); // Wait for this thread to complete
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting!");
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Calculate expected value if no race condition occurs
        int expectedValue = NUM_THREADS * INCREMENTS_PER_THREAD;

        // Print the final counter value and analysis
        System.out.println("\nFinal counter value: " + counter);
        System.out.println("Expected value: " + expectedValue);
        if (counter == expectedValue) {
            System.out.println("Result is correct: No race condition detected.");
        } else {
            System.out.println("Result is incorrect: Race condition occurred!");
            System.out.println("Difference: " + (expectedValue - counter) + " increments lost.");
        }
    }
}