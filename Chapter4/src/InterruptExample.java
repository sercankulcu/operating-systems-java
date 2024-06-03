/*
 * In Java, you can use the Thread.interrupt() method to interrupt a thread and the 
 * Thread.isInterrupted() method to check if a thread has been interrupted. Here is an example 
 * of how you can use these methods to handle interrupts in Java:
 * 
 * This code creates a thread that runs a loop and does some work in each iteration. The thread 
 * checks if it has been interrupted using the isInterrupted() method, and if it has, it exits 
 * the loop and terminates. The main thread creates the worker thread, starts it, waits for a 
 * while, interrupts the worker thread, and then waits for the worker thread to exit.
 * 
 * */

public class InterruptExample implements Runnable {
    @Override
    public void run() {
        while (true) {
            // Check if the thread has been interrupted
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread interrupted, exiting...");
                break;
            }

            // Do some work
            System.out.println("Working...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted, exiting...");
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptExample());
        thread.start();

        // Wait for a while
        Thread.sleep(3000);

        // Interrupt the thread
        thread.interrupt();

        // Wait for the thread to exit
        thread.join();
    }
}
