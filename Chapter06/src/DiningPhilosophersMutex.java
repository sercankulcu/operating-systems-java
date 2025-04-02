import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * This program implements the Dining Philosophers problem using mutex (locks).
 * Five philosophers sit around a table with five mutexes. Each philosopher needs
 * two mutexes to eat, and we use locks to prevent deadlock and ensure mutual exclusion.
 */
public class DiningPhilosophersMutex {
    
    // Number of philosophers (and mutexes)
    private static final int NUM_PHILOSOPHERS = 5;
    
    // Array of locks representing mutexes
    private static final Lock[] mutexes = new Lock[NUM_PHILOSOPHERS];

    // Initialize mutexes in a static block
    static {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            mutexes[i] = new ReentrantLock();
        }
    }

    // Philosopher class representing each dining philosopher
    static class Philosopher implements Runnable {
        private final int id;          // Philosopher's unique identifier (0 to 4)
        private final Lock leftFork;   // Left fork lock
        private final Lock rightFork;  // Right fork lock

        public Philosopher(int id) {
            this.id = id;
            // Assign mutexes: lower numbered fork first to prevent deadlock
            this.leftFork = mutexes[id];
            this.rightFork = mutexes[(id + 1) % NUM_PHILOSOPHERS];
        }

        // Simulate eating process
        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is trying to eat...");
            
            // Always lock the lower numbered fork first to avoid deadlock
            Lock firstFork = (id < (id + 1) % NUM_PHILOSOPHERS) ? leftFork : rightFork;
            Lock secondFork = (id < (id + 1) % NUM_PHILOSOPHERS) ? rightFork : leftFork;

            // Acquire first fork
            firstFork.lock();
            try {
                // Acquire second fork
                secondFork.lock();
                try {
                    System.out.println("Philosopher " + id + " is eating...");
                    Thread.sleep((long) (Math.random() * 1000)); // Simulate eating time
                    System.out.println("Philosopher " + id + " finished eating.");
                } finally {
                    // Release second fork
                    secondFork.unlock();
                }
            } finally {
                // Release first fork
                firstFork.unlock();
            }
        }

        // Simulate thinking process
        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking...");
            Thread.sleep((long) (Math.random() * 1000)); // Simulate thinking time
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) { // Each philosopher eats 3 times
                    think();
                    eat();
                }
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + id + " was interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Create array of philosopher threads
        Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];

        // Initialize and start philosopher threads
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Thread(new Philosopher(i));
            philosophers[i].start();
        }

        // Wait for all philosophers to finish
        for (Thread philosopher : philosophers) {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting!");
                Thread.currentThread().interrupt();
            }
        }

        // Final message when all philosophers are done
        System.out.println("All philosophers have finished dining!");
    }
}