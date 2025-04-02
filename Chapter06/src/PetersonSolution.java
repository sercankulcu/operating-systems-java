/*
 * This program implements Peterson's Algorithm for mutual exclusion between two threads.
 * It ensures that only one thread can enter its critical section at a time, using
 * shared variables (flags and turn) to coordinate access.
 */
public class PetersonSolution {
    
    // Shared variables for Peterson's Algorithm
    private static volatile boolean[] flag = new boolean[2]; // Indicates intent to enter critical section
    private static volatile int turn = 0;                   // Indicates whose turn it is (0 or 1)
    private static int counter = 0;                         // Shared resource to demonstrate critical section

    // Thread class representing a process using Peterson's Algorithm
    static class ProcessThread extends Thread {
        private final int id; // Thread ID (0 or 1)

        public ProcessThread(int id) {
            this.id = id;
        }

        // Enter the critical section using Peterson's Algorithm
        private void enterCriticalSection() {
            int other = 1 - id; // ID of the other thread (0 if id=1, 1 if id=0)

            // Step 1: Indicate intent to enter critical section
            flag[id] = true;
            
            // Step 2: Give priority to the other thread
            turn = other;
            
            // Step 3: Wait until the other thread is not interested or it's our turn
            while (flag[other] && turn == other) {
                // Busy wait (spin lock)
                Thread.yield(); // Yield to improve performance, optional
            }
        }

        // Exit the critical section
        private void exitCriticalSection() {
            // Indicate that this thread is no longer interested
            flag[id] = false;
        }

        // Simulate work in the critical section
        private void criticalSection() {
            int temp = counter;
            counter = temp + 1; // Increment shared counter
            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread " + id + " in critical section, counter = " + counter);
        }

        // Simulate non-critical section work
        private void nonCriticalSection() {
            try {
                Thread.sleep((long) (Math.random() * 100)); // Random delay 0-500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) { // Each thread performs 5 iterations
                // Non-critical section
                nonCriticalSection();
                
                // Enter critical section using Peterson's Algorithm
                enterCriticalSection();
                
                // Critical section
                criticalSection();
                
                // Exit critical section
                exitCriticalSection();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize flag array
        flag[0] = false;
        flag[1] = false;

        // Create two threads (processes)
        ProcessThread p0 = new ProcessThread(0);
        ProcessThread p1 = new ProcessThread(1);

        // Start the threads
        p0.start();
        p1.start();

        // Wait for both threads to finish
        try {
            p0.join();
            p1.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting!");
            Thread.currentThread().interrupt();
        }

        // Print final counter value
        System.out.println("Final counter value: " + counter);
    }
}