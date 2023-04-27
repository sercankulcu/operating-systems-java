import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Here is an example of a Java program that demonstrates the concept of a mutex 
 * (mutual exclusion object):
 * 
 * In this example, the incrementCounter method increments a counter variable. This method is 
 * called by two separate threads, which can potentially access the counter variable 
 * simultaneously. To prevent this, the method uses a ReentrantLock object as a mutex to create 
 * a critical region around the code that modifies the counter variable. 
 * 
 * The lock method is called to acquire the mutex, and the unlock method is called to release 
 * the mutex. The code that modifies the counter variable is placed between these two calls, 
 * ensuring that only one thread can access the critical region at a time.
 * 
 * */

public class MutexExample {
    private static final Lock mutex = new ReentrantLock();
    private static int counter = 0;

    public static void main(String[] args) {
        // Create two threads that increment the counter
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                incrementCounter();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                incrementCounter();
            }
        });
        // Start the threads
        t1.start();
        t2.start();
        // Wait for the threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Print the final value of the counter
        System.out.println("Final value of counter: " + counter);
    }

    private static void incrementCounter() {
        mutex.lock();
        try {
            counter++;
        } finally {
            mutex.unlock();
        }
    }
}
