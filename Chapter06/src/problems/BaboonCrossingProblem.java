package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Baboon Crossing Problem is a synchronization problem in operating systems that models a group of baboons 
 * crossing a canyon using a single rope. The problem, inspired by descriptions like those found on educational 
 * websites, involves the following constraints:
 * 
 * Baboons are on both sides of a canyon (east and west) and want to cross to the opposite side.
 * A single rope allows baboons to cross by swinging hand-over-hand (brachiation).
 * 
 * Constraints:
 * The rope has a limited capacity (e.g., 5 baboons at a time).
 * All baboons on the rope must move in the same direction to avoid deadlock 
 * (e.g., if eastbound and westbound baboons meet, they get stuck).
 * A baboon cannot join the rope if it’s at capacity or if baboons are moving in the opposite direction.
 * Continuous streams of baboons in one direction must not starve baboons wanting to cross in the opposite direction.
 * 
 * */

// Class to demonstrate the Baboon Crossing Problem using semaphores for synchronization
public class BaboonCrossingProblem {
    // Constants
    private static final int NUM_BABOONS = 10; // Number of baboons (split between east and west)
    private static final int ROPE_CAPACITY = 5; // Maximum number of baboons on the rope
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore mutex = new Semaphore(1); // Protects shared state
    private static final Semaphore eastQueue = new Semaphore(0); // Signals eastbound baboons
    private static final Semaphore westQueue = new Semaphore(0); // Signals westbound baboons
    private static final Semaphore ropeAccess = new Semaphore(ROPE_CAPACITY); // Limits rope capacity

    // Shared state
    private static int baboonsOnRope = 0; // Number of baboons currently on the rope
    private static String currentDirection = "none"; // Current direction: "east", "west", or "none"
    private static int waitingEast = 0; // Number of eastbound baboons waiting
    private static int waitingWest = 0; // Number of westbound baboons waiting

    // Baboon thread: Represents a baboon trying to cross the canyon
    static class Baboon extends Thread {
        private final int id;
        private final String direction; // "east" or "west"

        public Baboon(int id, String direction) {
            this.id = id;
            this.direction = direction;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate time before attempting to cross
                    Thread.sleep(random.nextInt(2000));

                    // Try to cross the rope
                    if (direction.equals("east")) {
                        crossEast();
                    } else {
                        crossWest();
                    }

                    // After crossing, simulate time before trying again
                    System.out.println("Baboon " + id + " (" + direction + ") reached other side");
                    Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void crossEast() throws InterruptedException {
            mutex.acquire(); // Protect shared state
            waitingEast++;
            System.out.println("Baboon " + id + " (east) wants to cross, " + waitingEast + " east, " + waitingWest + " west waiting");

            if (currentDirection.equals("west") || baboonsOnRope == ROPE_CAPACITY) {
                // Rope is in use by westbound or full, wait
                mutex.release();
                eastQueue.acquire();
                mutex.acquire();
            }

            // Rope is free or eastbound, allow crossing
            waitingEast--;
            baboonsOnRope++;
            currentDirection = "east";
            System.out.println("Baboon " + id + " (east) is mounting rope, " + baboonsOnRope + " on rope, direction: " + currentDirection);
            mutex.release();

            // Acquire rope slot
            ropeAccess.acquire();
            crossRope();

            // Dismount
            mutex.acquire(); // Protect shared state
            baboonsOnRope--;
            System.out.println("Baboon " + id + " (east) dismounted, " + baboonsOnRope + " on rope");
            ropeAccess.release(); // Release rope slot

            // If rope is empty, allow direction change
            if (baboonsOnRope == 0) {
                currentDirection = "none";
                // Wake waiting baboons in opposite direction first (fairness)
                if (waitingWest > 0) {
                    westQueue.release(waitingWest);
                } else if (waitingEast > 0) {
                    eastQueue.release(waitingEast);
                }
            }
            mutex.release();
        }

        private void crossWest() throws InterruptedException {
            mutex.acquire(); // Protect shared state
            waitingWest++;
            System.out.println("Baboon " + id + " (west) wants to cross, " + waitingEast + " east, " + waitingWest + " west waiting");

            if (currentDirection.equals("east") || baboonsOnRope == ROPE_CAPACITY) {
                // Rope is in use by eastbound or full, wait
                mutex.release();
                westQueue.acquire();
                mutex.acquire();
            }

            // Rope is free or westbound, allow crossing
            waitingWest--;
            baboonsOnRope++;
            currentDirection = "west";
            System.out.println("Baboon " + id + " (west) is mounting rope, " + baboonsOnRope + " on rope, direction: " + currentDirection);
            mutex.release();

            // Acquire rope slot
            ropeAccess.acquire();
            crossRope();

            // Dismount
            mutex.acquire(); // Protect shared state
            baboonsOnRope--;
            System.out.println("Baboon " + id + " (west) dismounted, " + baboonsOnRope + " on rope");
            ropeAccess.release(); // Release rope slot

            // If rope is empty, allow direction change
            if (baboonsOnRope == 0) {
                currentDirection = "none";
                // Wake waiting baboons in opposite direction first (fairness)
                if (waitingEast > 0) {
                    eastQueue.release(waitingEast);
                } else if (waitingWest > 0) {
                    westQueue.release(waitingWest);
                }
            }
            mutex.release();
        }

        private void crossRope() throws InterruptedException {
            System.out.println("Baboon " + id + " (" + direction + ") is crossing...");
            Thread.sleep(random.nextInt(500)); // Simulate crossing time
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Baboon Crossing Problem Simulation...");

        // Start baboon threads (half eastbound, half westbound)
        Baboon[] baboons = new Baboon[NUM_BABOONS];
        for (int i = 0; i < NUM_BABOONS; i++) {
            String direction = (i % 2 == 0) ? "east" : "west";
            baboons[i] = new Baboon(i + 1, direction);
            baboons[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(15000); // Run for 15 seconds
            for (Baboon b : baboons) {
                b.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}