import java.util.LinkedList;
import java.util.Queue;

/*
 * This program implements the Producer-Consumer problem using a synchronized queue.
 * Producers add items to a bounded queue, and consumers remove items from it.
 * Synchronization ensures that producers wait when the queue is full and consumers
 * wait when the queue is empty, preventing race conditions.
 */
public class ProducerConsumerSynchronized {
    
    // Shared queue with a fixed capacity
    private static final int CAPACITY = 5;
    private static final Queue<Integer> queue = new LinkedList<>();
    
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
                    
                    // Synchronize access to the shared queue
                    synchronized (queue) {
                        // Wait while the queue is full
                        while (queue.size() >= CAPACITY) {
                            System.out.println(name + " waiting: queue is full");
                            queue.wait(); // Release lock and wait
                        }
                        
                        // Add item to the queue
                        queue.add(item);
                        System.out.println(name + " produced: " + item + ", Queue size: " + queue.size());
                        
                        // Notify all waiting threads (consumers)
                        queue.notifyAll();
                    }
                    
                    // Simulate production time
                    Thread.sleep((long) (Math.random() * 500)); // Random delay 0-500ms
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
                    Integer item;
                    
                    // Synchronize access to the shared queue
                    synchronized (queue) {
                        // Wait while the queue is empty
                        while (queue.isEmpty()) {
                            System.out.println(name + " waiting: queue is empty");
                            queue.wait(); // Release lock and wait
                        }
                        
                        // Remove item from the queue
                        item = queue.poll();
                        System.out.println(name + " consumed: " + item + ", Queue size: " + queue.size());
                        
                        // Notify all waiting threads (producers)
                        queue.notifyAll();
                    }
                    
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
        Thread producer2 = new Thread(new Producer("Producer-2", 10));
        Thread consumer1 = new Thread(new Consumer("Consumer-1", 20));

        // Start the threads
        producer1.start();
        producer2.start();
        consumer1.start();

        // Wait for both threads to finish
        try {
            producer1.join();
            producer2.join();
            consumer1.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting!");
            Thread.currentThread().interrupt();
        }

        // Print completion message
        System.out.println("Producer-Consumer simulation completed!");
    }
}
