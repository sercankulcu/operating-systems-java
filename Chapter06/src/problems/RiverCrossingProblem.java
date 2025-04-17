package problems;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/*
 * 
 * The River Crossing Problem in the context of operating systems is a synchronization problem that models the coordination 
 * of multiple entities (e.g., hackers and serfs) trying to cross a river using a shared resource (a boat) with limited 
 * capacity and specific constraints. In this version, we consider a common variant where:
 * 
 * There are two types of threads: hackers and serfs.
 * A boat can carry exactly 4 passengers at a time to cross the river.
 * The boat can only depart when it has a valid combination of passengers: either 4 hackers, 4 serfs, or 2 hackers and 2 serfs.
 * One passenger must row the boat, and the boat returns automatically after each trip (simplified for this implementation).
 * Threads must synchronize to ensure valid boat compositions, avoid overloading, and prevent race conditions or deadlock.
 * 
 * */

// Class to demonstrate the River Crossing Problem using semaphores and CyclicBarrier for synchronization
public class RiverCrossingProblem {
    // Constants
    private static final int NUM_HACKERS = 8; // Number of hacker threads
    private static final int NUM_SERFS = 8;   // Number of serf threads
    private static final int BOAT_CAPACITY = 4; // Boat capacity
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore mutex = new Semaphore(1); // Ensures mutual exclusion for shared state
    private static final Semaphore hackerQueue = new Semaphore(0); // Signals hackers waiting to board
    private static final Semaphore serfQueue = new Semaphore(0);  // Signals serfs waiting to board
    private static final CyclicBarrier barrier = new CyclicBarrier(BOAT_CAPACITY, () -> {
        // Action executed when 4 passengers (valid combination) are ready
        System.out.println("Boat is full and crossing the river!");
    });

    // Shared state
    private static int waitingHackers = 0; // Number of hackers waiting to board
    private static int waitingSerfs = 0;   // Number of serfs waiting to board

    // Hacker thread: Represents a hacker trying to cross the river
    static class Hacker extends Thread {
        private final int id;

        public Hacker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(1000));

                    // Try to board the boat
                    mutex.acquire(); // Lock shared state
                    waitingHackers++;
                    System.out.println("Hacker " + id + " arrives, " + waitingHackers + " hackers, " + waitingSerfs + " serfs waiting");

                    if (waitingHackers >= 4) {
                        // Board 4 hackers
                        System.out.println("Hacker " + id + " is rowing, 4 hackers boarding");
                        releaseHackers(4);
                    } else if (waitingHackers >= 2 && waitingSerfs >= 2) {
                        // Board 2 hackers and 2 serfs
                        System.out.println("Hacker " + id + " is rowing, 2 hackers and 2 serfs boarding");
                        releaseHackers(2);
                        releaseSerfs(2);
                    } else {
                        // Not enough for a valid combination, wait
                        mutex.release(); // Unlock shared state
                        hackerQueue.acquire(); // Wait to board
                    }

                    // Board boat and cross river
                    boardBoat();
                    barrier.await();

                    // After crossing
                    System.out.println("Hacker " + id + " has crossed the river");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void boardBoat() {
            System.out.println("Hacker " + id + " is boarding the boat...");
        }
    }

    // Serf thread: Represents a serf trying to cross the river
    static class Serf extends Thread {
        private final int id;

        public Serf(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(1000));

                    // Try to board the boat
                    mutex.acquire(); // Lock shared state
                    waitingSerfs++;
                    System.out.println("Serf " + id + " arrives, " + waitingHackers + " hackers, " + waitingSerfs + " serfs waiting");

                    if (waitingSerfs >= 4) {
                        // Board 4 serfs
                        System.out.println("Serf " + id + " is rowing, 4 serfs boarding");
                        releaseSerfs(4);
                    } else if (waitingHackers >= 2 && waitingSerfs >= 2) {
                        // Board 2 hackers and 2 serfs
                        System.out.println("Serf " + id + " is rowing, 2 hackers and 2 serfs boarding");
                        releaseHackers(2);
                        releaseSerfs(2);
                    } else {
                        // Not enough for a valid combination, wait
                        mutex.release(); // Unlock shared state
                        serfQueue.acquire(); // Wait to board
                    }

                    // Board boat and cross river
                    boardBoat();
                    barrier.await();

                    // After crossing
                    System.out.println("Serf " + id + " has crossed the river");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void boardBoat() {
            System.out.println("Serf " + id + " is boarding the boat...");
        }
    }

    // Helper method to release a specified number of hackers
    private static void releaseHackers(int count) throws InterruptedException {
        mutex.acquire(); // Lock shared state
        waitingHackers -= count;
        hackerQueue.release(count); // Signal hackers to board
        mutex.release(); // Unlock shared state
    }

    // Helper method to release a specified number of serfs
    private static void releaseSerfs(int count) throws InterruptedException {
        mutex.acquire(); // Lock shared state
        waitingSerfs -= count;
        serfQueue.release(count); // Signal serfs to board
        mutex.release(); // Unlock shared state
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting River Crossing Problem Simulation...");

        // Start hacker threads
        Hacker[] hackers = new Hacker[NUM_HACKERS];
        for (int i = 0; i < NUM_HACKERS; i++) {
            hackers[i] = new Hacker(i + 1);
            hackers[i].start();
        }

        // Start serf threads
        Serf[] serfs = new Serf[NUM_SERFS];
        for (int i = 0; i < NUM_SERFS; i++) {
            serfs[i] = new Serf(i + 1);
            serfs[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(10000); // Run for 10 seconds
            for (Hacker h : hackers) {
                h.interrupt();
            }
            for (Serf s : serfs) {
                s.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}