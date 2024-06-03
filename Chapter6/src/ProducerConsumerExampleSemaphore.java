import java.util.concurrent.Semaphore;
import java.util.LinkedList;

/*
 * Here is an example of a Java program that demonstrates the producer-consumer problem using 
 * semaphores:
 * 
 * The Semaphore class is used to synchronize the producer and consumer threads and ensure that 
 * the producer does not add items to the queue when it is full, and the consumer does not remove 
 * items from the queue when it is empty. Three semaphores are used: full, empty, and mutex. The 
 * full semaphore is used to block the consumer when the queue is empty, and the empty semaphore 
 * is used to block the producer when the queue is full. The mutex semaphore is used to protect 
 * the shared queue from concurrent access.
 * */

public class ProducerConsumerExampleSemaphore {

	private static final int QUEUE_SIZE = 10;
	private static final LinkedList<Integer> queue = new LinkedList<>();
	private static final Semaphore full = new Semaphore(0);
	private static final Semaphore empty = new Semaphore(QUEUE_SIZE);
	private static final Semaphore mutex = new Semaphore(1);

	public static void main(String[] args) {
		// Create a producer thread
		Thread producer = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				try {
					empty.acquire();
					mutex.acquire();
					queue.add(i);
					System.out.println("Produced: " + i);
					mutex.release();
					full.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		// Create a consumer thread
		Thread consumer = new Thread(() -> {
			while (true) {
				try {
					full.acquire();
					mutex.acquire();
					int item = queue.remove();
					System.out.println("Consumed: " + item);
					mutex.release();
					empty.release();
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
