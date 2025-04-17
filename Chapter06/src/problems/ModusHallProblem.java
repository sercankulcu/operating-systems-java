package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Modus Hall Problem is a synchronization problem in operating systems that models a scenario where couples 
 * (each consisting of a man and a woman) must pair up in a dance hall to perform a dance, with a limited number 
 * of dance floor spots available. The problem, inspired by descriptions like those in educational resources, 
 * involves the following constraints:
 * 
 * Men and women arrive at the dance hall independently.
 * A couple (one man and one woman) must pair up to dance.
 * The dance floor has a limited capacity (e.g., 3 couples at a time).
 * Men and women wait in separate queues until a pair can be formed.
 * Once paired, a couple occupies a dance floor spot, dances, and leaves, freeing the spot.
 * 
 * Synchronization is needed to:
 * Ensure valid pairing (one man and one woman per couple).
 * Respect the dance floor capacity.
 * Prevent race conditions when forming pairs or accessing the dance floor.
 * Avoid deadlock or starvation (e.g., ensure men and women are paired fairly).
 * 
 * */

// Class to demonstrate the Modus Hall Problem using semaphores for synchronization
public class ModusHallProblem {
    // Constants
    private static final int NUM_MEN = 6; // Number of man threads
    private static final int NUM_WOMEN = 6; // Number of woman threads
    private static final int DANCE_FLOOR_CAPACITY = 3; // Maximum number of couples on the dance floor
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore mutex = new Semaphore(1); // Protects shared state
    private static final Semaphore manQueue = new Semaphore(0); // Signals men waiting to pair
    private static final Semaphore womanQueue = new Semaphore(0); // Signals women waiting to pair
    private static final Semaphore danceFloor = new Semaphore(DANCE_FLOOR_CAPACITY); // Limits dance floor capacity
    private static final Semaphore pairFormed = new Semaphore(0); // Signals when a couple is formed

    // Shared state
    private static int waitingMen = 0; // Number of men waiting to pair
    private static int waitingWomen = 0; // Number of women waiting to pair
    private static int dancingCouples = 0; // Number of couples currently dancing

    // Man thread: Represents a man trying to pair up and dance
    static class Man extends Thread {
        private final int id;

        public Man(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(2000));

                    // Try to pair with a woman
                    mutex.acquire(); // Protect shared state
                    waitingMen++;
                    System.out.println("Man " + id + " arrives, " + waitingMen + " men, " + waitingWomen + " women waiting");

                    if (waitingWomen > 0) {
                        // Pair with a waiting woman
                        waitingWomen--;
                        waitingMen--;
                        System.out.println("Man " + id + " pairs with a woman");
                        womanQueue.release(); // Signal paired woman
                        pairFormed.release(); // Signal couple formation
                    } else {
                        // No women available, wait
                        mutex.release();
                        manQueue.acquire();
                    }
                    mutex.release();

                    // Wait for couple formation
                    pairFormed.acquire();

                    // Dance as a couple
                    danceFloor.acquire(); // Occupy dance floor spot
                    mutex.acquire(); // Protect shared state
                    dancingCouples++;
                    System.out.println("Man " + id + " and partner start dancing, " + dancingCouples + " couples dancing");
                    mutex.release();
                    dance();

                    // Leave dance floor
                    mutex.acquire(); // Protect shared state
                    dancingCouples--;
                    System.out.println("Man " + id + " and partner finish dancing, " + dancingCouples + " couples dancing");
                    mutex.release();
                    danceFloor.release(); // Free dance floor spot
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void dance() throws InterruptedException {
            System.out.println("Man " + id + " is dancing...");
            Thread.sleep(random.nextInt(1000)); // Simulate dancing time (0-1 second)
        }
    }

    // Woman thread: Represents a woman trying to pair up and dance
    static class Woman extends Thread {
        private final int id;

        public Woman(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(2000));

                    // Try to pair with a man
                    mutex.acquire(); // Protect shared state
                    waitingWomen++;
                    System.out.println("Woman " + id + " arrives, " + waitingMen + " men, " + waitingWomen + " women waiting");

                    if (waitingMen > 0) {
                        // Pair with a waiting man
                        waitingMen--;
                        waitingWomen--;
                        System.out.println("Woman " + id + " pairs with a man");
                        manQueue.release(); // Signal paired man
                        pairFormed.release(); // Signal couple formation
                    } else {
                        // No men available, wait
                        mutex.release();
                        womanQueue.acquire();
                    }
                    mutex.release();

                    // Wait for couple formation
                    pairFormed.acquire();

                    // Dance as a couple
                    danceFloor.acquire(); // Occupy dance floor spot
                    mutex.acquire(); // Protect shared state
                    dancingCouples++;
                    System.out.println("Woman " + id + " and partner start dancing, " + dancingCouples + " couples dancing");
                    mutex.release();
                    dance();

                    // Leave dance floor
                    mutex.acquire(); // Protect shared state
                    dancingCouples--;
                    System.out.println("Woman " + id + " and partner finish dancing, " + dancingCouples + " couples dancing");
                    mutex.release();
                    danceFloor.release(); // Free dance floor spot
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void dance() throws InterruptedException {
            System.out.println("Woman " + id + " is dancing...");
            Thread.sleep(random.nextInt(1000)); // Simulate dancing time (0-1 second)
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Modus Hall Problem Simulation...");

        // Start man threads
        Man[] men = new Man[NUM_MEN];
        for (int i = 0; i < NUM_MEN; i++) {
            men[i] = new Man(i + 1);
            men[i].start();
        }

        // Start woman threads
        Woman[] women = new Woman[NUM_WOMEN];
        for (int i = 0; i < NUM_WOMEN; i++) {
            women[i] = new Woman(i + 1);
            women[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(15000); // Run for 15 seconds
            for (Man m : men) {
                m.interrupt();
            }
            for (Woman w : women) {
                w.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}