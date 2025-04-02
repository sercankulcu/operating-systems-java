/*
 * This program implements the Dining Philosophers problem using synchronized blocks.
 * Five philosophers sit around a table with five forks. Each philosopher needs two forks
 * to eat, and we use synchronized blocks to ensure mutual exclusion and prevent deadlock.
 */
public class DiningPhilosophersSynchronized {
    
    // Number of philosophers (and forks)
    private static final int NUM_PHILOSOPHERS = 5;
    
    // Array of objects representing forks (used as monitors for synchronization)
    private static final Object[] forks = new Object[NUM_PHILOSOPHERS];

    // Initialize forks in a static block
    static {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Object(); // Each fork is a unique object for synchronization
        }
    }

    // Philosopher class representing each dining philosopher
    static class Philosopher implements Runnable {
        private final int id;          // Philosopher's unique identifier (0 to 4)
        private final Object leftFork; // Left fork monitor
        private final Object rightFork;// Right fork monitor

        public Philosopher(int id) {
            this.id = id;
            // Assign forks: lower numbered fork first to prevent deadlock
            this.leftFork = forks[id];
            this.rightFork = forks[(id + 1) % NUM_PHILOSOPHERS];
        }

        // Simulate eating process
        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is trying to eat...");
            
            // Determine fork order to prevent deadlock (lower numbered fork first)
            Object firstFork = (id < (id + 1) % NUM_PHILOSOPHERS) ? leftFork : rightFork;
            Object secondFork = (id < (id + 1) % NUM_PHILOSOPHERS) ? rightFork : leftFork;

            // Synchronize on the first fork (acquire lock)
            synchronized (firstFork) {
                // Synchronize on the second fork (nested synchronization)
                synchronized (secondFork) {
                    System.out.println("Philosopher " + id + " is eating...");
                    Thread.sleep((long) (Math.random() * 1000)); // Simulate eating time (0-1 second)
                    System.out.println("Philosopher " + id + " finished eating.");
                }
                // Second fork lock is automatically released when exiting inner block
            }
            // First fork lock is automatically released when exiting outer block
        }

        // Simulate thinking process
        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking...");
            Thread.sleep((long) (Math.random() * 1000)); // Simulate thinking time (0-1 second)
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) { // Each philosopher eats 3 times
                    think(); // Think before eating
                    eat();   // Attempt to eat
                }
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + id + " was interrupted!");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

    public static void main(String[] args) {
        // Create array of philosopher threads
        Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];

        // Initialize and start philosopher threads
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Thread(new Philosopher(i), "Philosopher-" + i);
            philosophers[i].start(); // Start the philosopher thread
        }

        // Wait for all philosophers to finish dining
        for (Thread philosopher : philosophers) {
            try {
                philosopher.join(); // Wait for this philosopher to complete
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting!");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }

        // Print completion message when all philosophers are done
        System.out.println("All philosophers have finished dining!");
    }
}