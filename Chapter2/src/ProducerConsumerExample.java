import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Here is an example of a Java program that demonstrates the producer-consumer problem:
 * 
 * In this example, the producer thread generates a sequence of numbers and adds them to a shared 
 * queue. The consumer thread removes numbers from the queue and consumes them. 
 * 
 * The BlockingQueue class is used to synchronize the producer and consumer threads and ensure 
 * that the producer does not add items to the queue when it is full, and the consumer does not 
 * remove items from the queue when it is empty.
 * 
 * */

public class ProducerConsumerExample {
    private static final int QUEUE_SIZE = 10;
    private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);

    public static void main(String[] args) {
        // Create a producer thread
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Create a consumer thread
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    int item = queue.take();
                    System.out.println("Consumed: " + item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Start the threads
        producer.start();
        consumer.start();
    }
}
