/*
 * Here is an example of a Java program that demonstrates a race condition:
 * 
 * In this example, the counter variable is shared by two threads, which both increment it 1000 
 * times. However, because the threads are running concurrently and accessing the counter variable 
 * without synchronization, the final value of the counter variable is not guaranteed to be 2000.
 * 
 * */

public class RaceConditionExample {
    private static int counter = 0;

    public static void main(String[] args) {
        // Create two threads that increment the counter
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        }).start();
        // Wait for the threads to finish
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Print the counter
        System.out.println("Counter: " + counter);
    }
}
