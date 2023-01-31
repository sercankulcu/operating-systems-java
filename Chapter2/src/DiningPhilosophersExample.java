import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Here is an example of a Java program that demonstrates the "dining philosophers" problem 
 * using mutexes (also known as locks) to synchronize access to the forks:
 * 
 * circular wait condition might arise
 * 
 * */

public class DiningPhilosophersExample {
    private static final int NUM_PHILOSOPHERS = 5;
    private static final Lock[] mutexes = new ReentrantLock[NUM_PHILOSOPHERS];

    public static void main(String[] args) {
        // Initialize the mutexes
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            mutexes[i] = new ReentrantLock();
        }
        // Create the philosophers
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            int left = i;
            int right = (i + 1) % NUM_PHILOSOPHERS;
            new Thread(() -> {
                while (true) {
                    // Think
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Acquire left mutex
                    mutexes[left].lock();
                    try {
                        // Acquire right mutex
                        mutexes[right].lock();
                        try {
                            // Eat
                            System.out.println("Philosopher " + left + " is eating");
                        } finally {
                            // Release right mutex
                            mutexes[right].unlock();
                        }
                    } finally {
                        // Release left mutex
                        mutexes[left].unlock();
                    }
                }
            }).start();
        }
    }
}
