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
 * Extensions:
 * 1. Add a cleanup phase after interruption.
 * 2. Demonstrate how to restore the interrupt status if needed.
 * 3. Handle multiple interruptions.
 * 4. Introduce a shared resource to manage the work more complexly.
 * 5. Add a timeout to the work.
 * 
 * */

public class InterruptExample implements Runnable {
	
    private volatile boolean isRunning = true;
    private Object sharedResource = new Object(); // Example of a shared resource

    @Override
    public void run() {
        while (isRunning) {
            // Check if the thread has been interrupted
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread interrupted, exiting...");
                cleanup();
                // Restore interrupt status if needed
                // Thread.currentThread().interrupt();
                break;
            }

            // Do some work with a timeout.
            try {
                doWorkWithTimeout();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted during work, exiting...");
                cleanup();
                // Restore interrupt status if needed
                // Thread.currentThread().interrupt();
                break;
            }
            
            // Example of using shared resource
            synchronized (sharedResource) {
                System.out.println("Working with shared resource...");
            }
        }
    }

    private void doWorkWithTimeout() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long timeout = 2000; // 2 seconds timeout

        while (System.currentTimeMillis() - startTime < timeout) {
            System.out.println("Working...");
            Thread.sleep(500); // Simulate work
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
        }
    }

    private void cleanup() {
        System.out.println("Performing cleanup...");
        // Perform cleanup tasks here (e.g., closing resources)
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptExample worker = new InterruptExample();
        Thread thread = new Thread(worker);
        thread.start();

        // Wait for a while
        Thread.sleep(5000);

        // Interrupt the thread
        thread.interrupt();

        // Wait for the thread to exit
        thread.join();

        System.out.println("Main thread finished.");
    }
}