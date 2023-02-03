
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    Queue<Object> messageQueue;
    int messageQueueSize;

    ProducerConsumer(int messageQueueSize) {
        this.messageQueue = new LinkedList<>();
        this.messageQueueSize = messageQueueSize;
    }

    void produce(Object object) {
        synchronized (this) {
            if (messageQueue.size() == messageQueueSize) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
            messageQueue.offer(object);
            notifyAll();
        }
    }

    void consume() {
        while (true) {
            synchronized (this) {
                if (messageQueue.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                System.out.println(messageQueue.poll().toString());
                notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer producerConsumer = new ProducerConsumer(2);
        Thread thread = new Thread(() -> producerConsumer.produce(new String("Thread1")));
        Thread thread2 = new Thread(() -> producerConsumer.produce(new String("Thread2")));
        Thread thread3 = new Thread(() -> producerConsumer.produce(new String("Thread3")));
        Thread consumerThread = new Thread(() -> producerConsumer.consume());

        thread.start();
        thread2.start();
        thread3.start();

        Thread.sleep(10000);

        consumerThread.start();
    }

}