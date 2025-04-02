import java.util.concurrent.Semaphore;

/*
 * This program implements the Producer-Consumer problem using semaphores.
 * Producers add items to a bounded buffer, and consumers remove items from it.
 * Semaphores are used to manage buffer capacity and ensure mutual exclusion.
 */
public class ProducerConsumerSemaphore {
    
    // Buffer size and shared buffer array
    private static final int BUFFER_SIZE = 5;
    private static final int[] buffer = new int[BUFFER_SIZE];
    private static int in = 0;  // Index for next item to produce
    private static int out = 0; // Index for next item to consume
    private static int count = 0; // Number of items currently in buffer

    // Semaphores for synchronization
    private static final Semaphore mutex = new Semaphore(1);      // Mutual exclusion for buffer access
    private static final Semaphore empty = new Semaphore(BUFFER_SIZE); // Counts empty slots
    private static final Semaphore full = new Semaphore(0);       // Counts filled slots

    // Producer class that adds items to the buffer
    static class Producer implements Runnable {
        private final String name;
        private int itemCount; // Number of items to produce

        public Producer(String name, int itemCount) {
            this.name = name;
            this.itemCount = itemCount;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < itemCount; i++) {
                    int item = (int) (Math.random() * 100); // Generate random item (0-99)
                    
                    // Wait for an empty slot
                    empty.acquire();
                    
                    // Acquire mutex to access buffer
                    mutex.acquire();
                    try {
                        // Add item to buffer
                        buffer[in] = item;
                        in = (in + 1) % BUFFER_SIZE;
                        count++;
                        System.out.println(name + " produced: " + item + ", Buffer count: " + count);
                    } finally {
                        // Release mutex
                        mutex.release();
                    }
                    
                    // Signal that a slot is now full
                    full.release();
                    
                    // Simulate production time
                    Thread.sleep((long) (Math.random() * 500)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Consumer class that removes items from the buffer
    static class Consumer implements Runnable {
        private final String name;
        private int itemCount; // Number of items to consume

        public Consumer(String name, int itemCount) {
            this.name = name;
            this.itemCount = itemCount;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < itemCount; i++) {
                    // Wait for a full slot
                    full.acquire();
                    
                    // Acquire mutex to access buffer
                    mutex.acquire();
                    try {
                        // Remove item from buffer
                        int item = buffer[out];
                        out = (out + 1) % BUFFER_SIZE;
                        count--;
                        System.out.println(name + " consumed: " + item + ", Buffer count: " + count);
                    } finally {
                        // Release mutex
                        mutex.release();
                    }
                    
                    // Signal that a slot is now empty
                    empty.release();
                    
                    // Simulate consumption time
                    Thread.sleep((long) (Math.random() * 500)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Create producer and consumer threads
        Thread producer1 = new Thread(new Producer("Producer-1", 10));
        Thread consumer1 = new Thread(new Consumer("Consumer-1", 10));

        // Start the threads
        producer1.start();
        consumer1.start();

        // Wait for both threads to finish
        try {
            producer1.join();
            consumer1.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting!");
            Thread.currentThread().interrupt();
        }

        // Print completion message
        System.out.println("Producer-Consumer simulation completed!");
    }
}