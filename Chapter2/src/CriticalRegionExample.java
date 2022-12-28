import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CriticalRegionExample {
    private static final Lock lock = new ReentrantLock();
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
        lock.lock();
        try {
            counter++;
            System.out.println("Value of counter: " + counter);
        } finally {
            lock.unlock();
        }
    }
}
