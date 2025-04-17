import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

/*
 * This program demonstrates the use of CyclicBarrier in Java.
 * Multiple worker threads will wait at a barrier until all reach it,
 * then proceed together. The barrier can be reused (cyclic).
 */
public class CyclicBarrierr{
    
    // Number of threads that need to reach the barrier
    private static final int NUMBER_OF_THREADS = 3;
    
    // CyclicBarrier instance with a barrier action that runs when all threads arrive
    private static final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS, () -> {
        System.out.println("All threads have reached the barrier. Proceeding together!");
    });

    // Worker thread class that simulates some work and waits at the barrier
    static class Worker implements Runnable {
        private final String name;
        
        public Worker(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                // Simulate some initial work
                System.out.println(name + " is doing initial work...");
                Thread.sleep((long) (Math.random() * 2000)); // Random delay 0-2 seconds
                
                // Reach the barrier and wait for others
                System.out.println(name + " has reached the barrier, waiting...");
                barrier.await();
                
                // This code runs after all threads pass the barrier
                System.out.println(name + " has passed the barrier and is continuing work...");
                Thread.sleep((long) (Math.random() * 1000)); // Random delay 0-1 second
                
                // Demonstrate cyclic nature by reaching barrier again
                System.out.println(name + " has reached the barrier again, waiting...");
                barrier.await();
                
                // Final work after second barrier
                System.out.println(name + " has completed all work!");
                
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted!");
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                System.out.println(name + " encountered a broken barrier!");
            }
        }
    }

    public static void main(String[] args) {
        // Create an array to hold our worker threads
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        
        // Initialize and start worker threads
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(new Worker("Worker-" + (i + 1)));
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting!");
                Thread.currentThread().interrupt();
            }
        }
        
        // Final message when all threads are done
        System.out.println("All workers have completed their tasks!");
    }
}