package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The problem involves Santa Claus, a group of elves, and a group of reindeer, all interacting with specific 
 * synchronization constraints:
 * 
 * Santa Claus: Sleeps until either all reindeer are back (to deliver toys) or a group of elves needs help 
 * (to solve toy-making issues).
 * Reindeer: Return from vacation one by one. When all reindeer are back, they wake Santa to prepare the 
 * sleigh and deliver toys.
 * Elves: Work on toy-making but occasionally need Santa’s help. A fixed number of elves (e.g., 3) must group 
 * together to wake Santa, while others wait.
 * 
 * */

// Class to demonstrate the Santa Claus Problem using semaphores for synchronization
public class SantaClausProblem {
    // Constants
    private static final int NUM_REINDEER = 9; // Number of reindeer
    private static final int NUM_ELVES = 10; // Number of elves
    private static final int ELVES_PER_GROUP = 3; // Number of elves needed to wake Santa
    private static final Random random = new Random(); // Random number generator for delays

    // Semaphores for synchronization
    private static final Semaphore santaSem = new Semaphore(0); // Signals Santa to wake up
    private static final Semaphore reindeerSem = new Semaphore(0); // Signals reindeer to harness
    private static final Semaphore elfMutex = new Semaphore(1); // Mutex for elf group coordination
    private static final Semaphore elfSem = new Semaphore(0); // Signals elves to get help
    private static final Semaphore mutex = new Semaphore(1); // Mutex for shared state

    // Shared state
    private static int waitingReindeer = 0; // Number of reindeer waiting
    private static int waitingElves = 0; // Number of elves waiting for Santa
    private static boolean isReindeerTask = false; // Tracks if reindeer task is in progress

    // Santa thread: Sleeps until woken by reindeer or elves, prioritizes reindeer
    static class Santa extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // Wait for reindeer or elves to wake Santa
                    System.out.println("Santa is sleeping...");
                    santaSem.acquire();

                    mutex.acquire(); // Lock shared state
                    if (waitingReindeer == NUM_REINDEER) {
                        // All reindeer are back, prepare sleigh
                        isReindeerTask = true;
                        System.out.println("Santa wakes up to prepare sleigh for " + waitingReindeer + " reindeer");
                        prepareSleigh();
                        reindeerSem.release(NUM_REINDEER); // Signal all reindeer
                        waitingReindeer = 0; // Reset reindeer count
                        isReindeerTask = false;
                    } else if (waitingElves == ELVES_PER_GROUP && !isReindeerTask) {
                        // Group of elves needs help
                        System.out.println("Santa wakes up to help " + waitingElves + " elves");
                        helpElves();
                        elfSem.release(ELVES_PER_GROUP); // Signal elves
                        waitingElves = 0; // Reset elf count
                    }
                    mutex.release(); // Unlock shared state
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void prepareSleigh() throws InterruptedException {
            System.out.println("Santa is preparing the sleigh...");
            Thread.sleep(random.nextInt(1000)); // Simulate sleigh preparation
            System.out.println("Sleigh is ready, delivering toys!");
        }

        private void helpElves() throws InterruptedException {
            System.out.println("Santa is helping elves...");
            Thread.sleep(random.nextInt(1000)); // Simulate helping elves
            System.out.println("Santa finished helping elves");
        }
    }

    // Reindeer thread: Returns from vacation and waits for Santa
    static class Reindeer extends Thread {
        private final int id;

        public Reindeer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate vacation time
                    Thread.sleep(random.nextInt(2000));

                    // Return and join waiting reindeer
                    mutex.acquire(); // Lock shared state
                    waitingReindeer++;
                    System.out.println("Reindeer " + id + " returns, " + waitingReindeer + " waiting");

                    if (waitingReindeer == NUM_REINDEER) {
                        // All reindeer are back, wake Santa
                        System.out.println("All reindeer back, waking Santa!");
                        santaSem.release();
                    }
                    mutex.release(); // Unlock shared state

                    // Wait for Santa to prepare sleigh
                    reindeerSem.acquire();
                    getHitched();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void getHitched() throws InterruptedException {
            System.out.println("Reindeer " + id + " is getting hitched...");
            Thread.sleep(random.nextInt(500)); // Simulate hitching time
        }
    }

    // Elf thread: Works independently but occasionally needs Santa's help
    static class Elf extends Thread {
        private final int id;

        public Elf(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate working time
                    Thread.sleep(random.nextInt(3000));

                    // Try to join group to get Santa's help
                    elfMutex.acquire(); // Lock elf group coordination
                    mutex.acquire(); // Lock shared state
                    if (!isReindeerTask) { // Only proceed if no reindeer task
                        waitingElves++;
                        System.out.println("Elf " + id + " needs help, " + waitingElves + " waiting");
                        if (waitingElves == ELVES_PER_GROUP) {
                            // Group complete, wake Santa
                            System.out.println("Group of " + ELVES_PER_GROUP + " elves complete, waking Santa!");
                            santaSem.release();
                        }
                        mutex.release(); // Unlock shared state
                        elfMutex.release(); // Unlock elf group coordination

                        // Wait for Santa's help
                        elfSem.acquire();
                        getHelp();
                    } else {
                        // Reindeer task in progress, try again later
                        System.out.println("Elf " + id + " waits, reindeer task in progress");
                        mutex.release(); // Unlock shared state
                        elfMutex.release(); // Unlock elf group coordination
                        Thread.sleep(random.nextInt(1000)); // Wait before retrying
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void getHelp() throws InterruptedException {
            System.out.println("Elf " + id + " is getting help from Santa...");
            Thread.sleep(random.nextInt(500)); // Simulate help time
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Santa Claus Problem Simulation...");

        // Start Santa thread
        Santa santa = new Santa();
        santa.start();

        // Start reindeer threads
        Reindeer[] reindeer = new Reindeer[NUM_REINDEER];
        for (int i = 0; i < NUM_REINDEER; i++) {
            reindeer[i] = new Reindeer(i + 1);
            reindeer[i].start();
        }

        // Start elf threads
        Elf[] elves = new Elf[NUM_ELVES];
        for (int i = 0; i < NUM_ELVES; i++) {
            elves[i] = new Elf(i + 1);
            elves[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(15000); // Run for 15 seconds
            santa.interrupt();
            for (Reindeer r : reindeer) {
                r.interrupt();
            }
            for (Elf e : elves) {
                e.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}