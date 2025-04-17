package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Dining Savages Problem is a classic synchronization problem in operating systems that illustrates concurrency 
 * control, resource sharing, and process coordination. The problem involves a group of savages who share a pot of food, 
 * a cook who refills the pot when it is empty, and the need to synchronize access to ensure that savages only eat when 
 * food is available and the cook only refills an empty pot. This problem demonstrates key operating system concepts 
 * such as mutual exclusion, condition synchronization, and avoiding deadlock or starvation.
 * 
 * Savages: A group of savages (5 in this implementation) who periodically get hungry and try to take a serving from 
 * a shared pot.
 * Cook: A single cook who refills the pot with a fixed number of servings (10) when it becomes empty.
 * Pot: A shared resource with a limited capacity, represented by the servings variable.
 * The goal is to synchronize access so that:
 * Savages can only eat if there are servings in the pot.
 * The cook only refills the pot when it is empty.
 * Multiple savages do not access the pot simultaneously, causing inconsistencies.
 * 
 * */

// Class to demonstrate the Dining Savages Problem using semaphores for synchronization
public class DiningSavagesProblem {
    // Constants
    private static final int POT_CAPACITY = 10; // Maximum servings in the pot
    private static final int NUM_SAVAGES = 5;  // Number of savage threads
    private static final Random random = new Random(); // Random number generator for delays

    // Semaphores for synchronization
    private static final Semaphore mutex = new Semaphore(1); // Ensures mutual exclusion for shared state
    private static final Semaphore potFull = new Semaphore(0); // Signals when pot has servings
    private static final Semaphore potEmpty = new Semaphore(0); // Signals when pot is empty

    // Shared state
    private static int servings = POT_CAPACITY; // Current number of servings in the pot

    // Savage thread: Represents a savage trying to eat from the pot
    static class Savage extends Thread {
        private final int id;

        public Savage(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate hunger (random delay)
                    Thread.sleep(random.nextInt(1000));

                    // Try to get a serving
                    mutex.acquire(); // Lock shared state
                    if (servings == 0) {
                        // Pot is empty, signal cook to refill
                        System.out.println("Savage " + id + " finds pot empty, waking cook...");
                        potEmpty.release();
                        mutex.release(); // Unlock before waiting
                        potFull.acquire(); // Wait for pot to be refilled
                        mutex.acquire(); // Re-lock to check servings
                    }

                    // Take a serving
                    servings--;
                    System.out.println("Savage " + id + " takes a serving, " + servings + " servings left");
                    mutex.release(); // Unlock shared state

                    // Eat
                    eat();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void eat() throws InterruptedException {
            System.out.println("Savage " + id + " is eating...");
            Thread.sleep(random.nextInt(500)); // Simulate eating time
        }
    }

    // Cook thread: Refills the pot when it is empty
    static class Cook extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // Wait for pot to be empty
                    potEmpty.acquire();
                    mutex.acquire(); // Lock shared state

                    // Refill pot
                    servings = POT_CAPACITY;
                    System.out.println("Cook refills pot to " + servings + " servings");

                    mutex.release(); // Unlock shared state
                    potFull.release(); // Signal savages that pot is full
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Dining Savages Problem Simulation...");

        // Create and start cook thread
        Cook cook = new Cook();
        cook.start();

        // Create and start savage threads
        Savage[] savages = new Savage[NUM_SAVAGES];
        for (int i = 0; i < NUM_SAVAGES; i++) {
            savages[i] = new Savage(i + 1);
            savages[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(10000); // Run for 10 seconds
            cook.interrupt();
            for (Savage savage : savages) {
                savage.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}