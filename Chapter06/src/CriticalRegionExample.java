// Import necessary classes for thread synchronization
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Here is an example of a Java program that demonstrates the use of a critical region:
 * This program shows how to use locks to protect shared resources in a multi-threaded environment.
 * Two threads will concurrently increment a shared counter, and the Lock ensures thread safety.
 */
public class CriticalRegionExample {
    
    // Declare a static Lock object using ReentrantLock for thread synchronization
    private static final Lock lock = new ReentrantLock();
    
    // Shared counter variable that will be accessed by multiple threads
    private static int counter = 0;

    public static void main(String[] args) {
        // Create first thread that will increment the counter 100,000 times
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                // Call the synchronized increment method
                incrementCounter();
            }
        }); 
        
        // Create second thread that will also increment the counter 100,000 times
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                // Call the synchronized increment method
                incrementCounter();
            }
        });

        // Start both threads to begin execution
        t1.start();
        t2.start();

        // Wait for both threads to complete their execution
        try {
            t1.join();  // Wait for thread 1 to finish
            t2.join();  // Wait for thread 2 to finish
        } catch (InterruptedException e) {
            // Handle potential interruption during thread joining
            e.printStackTrace();
        }

        // Print the final value of the counter after both threads complete
        // Expected value should be 200,000 if synchronization works correctly
        System.out.println("Final value of counter: " + counter);
    }

    // Method to increment the counter within a critical region
    private static void incrementCounter() {
        // Acquire the lock to enter the critical region
        lock.lock();
        try {
            // Critical region: only one thread can execute this block at a time
            // Print current value and increment counter
            System.out.println("Value of counter: " + counter++);
        } finally {
            // Ensure the lock is released even if an exception occurs
            // This prevents deadlock situations
            lock.unlock();
        }
    }
}