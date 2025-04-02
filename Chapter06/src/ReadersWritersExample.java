import java.util.concurrent.Semaphore;

/*
 * This program demonstrates the Readers-Writers problem, a classic concurrency challenge.
 * Multiple reader threads can read a shared resource simultaneously, but writer threads
 * require exclusive access. Semaphores are used to synchronize access:
 * - Readers can read concurrently but block writers when active.
 * - Writers have exclusive access, blocking all readers and other writers.
 */
public class ReadersWritersExample {

    // Shared variables for synchronization and resource tracking
    private static int readCount = 0;                // Number of active readers
    private static final Semaphore mutex = new Semaphore(1);      // Protects readCount
    private static final Semaphore writeLock = new Semaphore(1);  // Controls write access
    private static int sharedResource = 0;           // Simulated shared resource (e.g., a counter)
    private static final int MAX_VALUE = 10;         // Max value for writers to increment to

    // Reader class representing threads that read the shared resource
    static class Reader implements Runnable {
        private final String name;

        public Reader(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) { // Each reader reads 3 times
                    // Acquire mutex to update readCount safely
                    mutex.acquire();
                    readCount++;
                    if (readCount == 1) {
                        // First reader blocks writers by acquiring writeLock
                        writeLock.acquire();
                        System.out.println(name + " blocked writers (first reader).");
                    }
                    mutex.release();

                    // Critical section: reading the shared resource
                    System.out.println(name + " is reading: sharedResource = " + sharedResource);
                    Thread.sleep((long) (Math.random() * 1000)); // Simulate reading time (0-1s)

                    // Acquire mutex to update readCount on exit
                    mutex.acquire();
                    readCount--;
                    System.out.println(name + " finished reading, active readers: " + readCount);
                    if (readCount == 0) {
                        // Last reader releases writeLock, allowing writers
                        writeLock.release();
                        System.out.println(name + " unblocked writers (last reader).");
                    }
                    mutex.release();

                    // Simulate time between reads
                    Thread.sleep((long) (Math.random() * 500)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Writer class representing threads that modify the shared resource
    static class Writer implements Runnable {
        private final String name;

        public Writer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) { // Each writer writes 3 times
                    // Acquire writeLock for exclusive access
                    writeLock.acquire();
                    System.out.println(name + " acquired write lock.");

                    // Critical section: writing to the shared resource
                    if (sharedResource < MAX_VALUE) {
                        sharedResource++;
                        System.out.println(name + " is writing: sharedResource = " + sharedResource);
                    } else {
                        System.out.println(name + " found max value reached: " + sharedResource);
                    }
                    Thread.sleep((long) (Math.random() * 1000)); // Simulate writing time (0-1s)

                    // Release writeLock after writing
                    writeLock.release();
                    System.out.println(name + " released write lock.");

                    // Simulate time between writes
                    Thread.sleep((long) (Math.random() * 500)); // Random delay 0-500ms
                }
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Define number of readers and writers
        int numReaders = 3;
        int numWriters = 2;
        Thread[] threads = new Thread[numReaders + numWriters];

        // Create and start reader threads
        for (int i = 0; i < numReaders; i++) {
            threads[i] = new Thread(new Reader("Reader-" + (i + 1)));
            threads[i].start();
            System.out.println("Started " + threads[i].getName());
        }

        // Create and start writer threads
        for (int i = 0; i < numWriters; i++) {
            threads[numReaders + i] = new Thread(new Writer("Writer-" + (i + 1)));
            threads[numReaders + i].start();
            System.out.println("Started " + threads[numReaders + i].getName());
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join(); // Wait for this thread to complete
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted while waiting!");
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Print final state of the shared resource
        System.out.println("\nSimulation completed! Final sharedResource value: " + sharedResource);
    }
}