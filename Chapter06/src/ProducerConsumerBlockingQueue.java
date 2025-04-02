import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/*
 * This program implements the Producer-Consumer problem using a BlockingQueue.
 * Producers add items to a bounded queue, and consumers remove items from it.
 * BlockingQueue handles synchronization and waiting automatically, simplifying
 * the coordination between producers and consumers.
 */
public class ProducerConsumerBlockingQueue {
    
    // Shared BlockingQueue with a fixed capacity
    private static final int CAPACITY = 5;
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(CAPACITY);

    // Producer class that adds items to the queue
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
                    
                    // Add item to the queue; blocks if queue is full
                    queue.put(item);
                    System.out.println(name + " produced: " + item + ", Queue size: " + queue.size());
                    
                    // Simulate production time
                    Thread.sleep((long) (Math.random() * 100)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Consumer class that removes items from the queue
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
                    // Remove item from the queue; blocks if queue is empty
                    Integer item = queue.take();
                    System.out.println(name + " consumed: " + item + ", Queue size: " + queue.size());
                    
                    // Simulate consumption time
                    Thread.sleep((long) (Math.random() * 200)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Create producer and consumer threads
        Thread producer1 = new Thread(new Producer("Producer-1", 20));
        Thread consumer1 = new Thread(new Consumer("Consumer-1", 10));
        Thread consumer2 = new Thread(new Consumer("Consumer-2", 10));

        // Start the threads
        producer1.start();
        consumer1.start();
        consumer2.start();

        // Wait for both threads to finish
        try {
            producer1.join();
            consumer1.join();
            consumer2.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting!");
            Thread.currentThread().interrupt();
        }

        // Print completion message
        System.out.println("Producer-Consumer simulation completed!");
    }
}