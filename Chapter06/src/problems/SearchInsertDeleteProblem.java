package problems;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Search-Insert-Delete Problem is a synchronization problem in operating systems that models concurrent access 
 * to a shared data structure (e.g., a list) by multiple threads performing search, insert, and delete operations. 
 * The challenge is to ensure thread safety, prevent race conditions, and maintain data consistency while allowing 
 * concurrent operations. This problem demonstrates key operating system concepts such as mutual exclusion, 
 * condition synchronization, and resource management.
 * 
 * In this implementation:
 * 
 * A shared list (e.g., a linked list) stores integers.
 * Reader threads search for elements in the list.
 * Writer threads perform insert or delete operations.
 * 
 * To ensure correctness:
 * Multiple readers can access the list simultaneously (readers do not modify the list).
 * Writers require exclusive access (insert or delete modifies the list).
 * Readers and writers must synchronize to avoid race conditions or inconsistent reads.
 * A priority mechanism can be used to prevent writer starvation (e.g., writers get preference after waiting).
 * 
 * */

// Class to demonstrate the Search-Insert-Delete Problem using semaphores for synchronization
public class SearchInsertDeleteProblem {
    // Constants
    private static final int NUM_READERS = 6; // Number of reader threads
    private static final int NUM_WRITERS = 3; // Number of writer threads
    private static final Random random = new Random(); // Random number generator for delays and operations

    // Shared data structure: a list of integers
    private static final ArrayList<Integer> sharedList = new ArrayList<>();

    // Synchronization primitives
    private static final Semaphore mutex = new Semaphore(1); // Protects shared counters
    private static final Semaphore writeLock = new Semaphore(1); // Ensures exclusive write access
    private static final Semaphore readerEntry = new Semaphore(1); // Controls reader entry to prevent writer starvation
    private static int readerCount = 0; // Tracks active readers

    // Reader thread: Searches for an element in the shared list
    static class Reader extends Thread {
        private final int id;

        public Reader(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate time between searches
                    Thread.sleep(random.nextInt(1000));

                    // Entry protocol for readers
                    readerEntry.acquire(); // Prevent writer starvation
                    mutex.acquire(); // Protect readerCount
                    readerCount++;
                    if (readerCount == 1) {
                        writeLock.acquire(); // First reader locks out writers
                    }
                    mutex.release();
                    readerEntry.release();

                    // Perform search
                    int searchValue = random.nextInt(100); // Random value to search
                    search(searchValue);

                    // Exit protocol for readers
                    mutex.acquire(); // Protect readerCount
                    readerCount--;
                    if (readerCount == 0) {
                        writeLock.release(); // Last reader allows writers
                    }
                    mutex.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void search(int value) {
            synchronized (sharedList) { // Ensure consistent read
                boolean found = sharedList.contains(value);
                System.out.println("Reader " + id + " searched for " + value + ", found: " + found +
                        ", list size: " + sharedList.size());
            }
        }
    }

    // Writer thread: Performs insert or delete operations on the shared list
    static class Writer extends Thread {
        private final int id;

        public Writer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate time between operations
                    Thread.sleep(random.nextInt(1500));

                    // Entry protocol for writers
                    readerEntry.acquire(); // Block new readers
                    writeLock.acquire(); // Ensure exclusive access

                    // Perform insert or delete
                    if (random.nextBoolean()) {
                        insert();
                    } else {
                        delete();
                    }

                    // Exit protocol for writers
                    writeLock.release();
                    readerEntry.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void insert() {
            int value = random.nextInt(100); // Random value to insert
            synchronized (sharedList) {
                sharedList.add(value);
                System.out.println("Writer " + id + " inserted " + value + ", list size: " + sharedList.size());
            }
        }

        private void delete() {
            synchronized (sharedList) {
                if (!sharedList.isEmpty()) {
                    int index = random.nextInt(sharedList.size());
                    int value = sharedList.remove(index);
                    System.out.println("Writer " + id + " deleted " + value + ", list size: " + sharedList.size());
                } else {
                    System.out.println("Writer " + id + " tried to delete, but list is empty");
                }
            }
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Search-Insert-Delete Problem Simulation...");

        // Initialize shared list with some values
        synchronized (sharedList) {
            for (int i = 0; i < 5; i++) {
                sharedList.add(random.nextInt(100));
            }
            System.out.println("Initial list: " + sharedList);
        }

        // Start reader threads
        Reader[] readers = new Reader[NUM_READERS];
        for (int i = 0; i < NUM_READERS; i++) {
            readers[i] = new Reader(i + 1);
            readers[i].start();
        }

        // Start writer threads
        Writer[] writers = new Writer[NUM_WRITERS];
        for (int i = 0; i < NUM_WRITERS; i++) {
            writers[i] = new Writer(i + 1);
            writers[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(10000); // Run for 10 seconds
            for (Reader r : readers) {
                r.interrupt();
            }
            for (Writer w : writers) {
                w.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended. Final list: " + sharedList);
    }
}