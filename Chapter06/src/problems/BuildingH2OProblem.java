package problems;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/*
 * The Building H2O Problem is a synchronization problem in operating systems that models the creation of water 
 * molecules (H₂O) by coordinating hydrogen and oxygen atoms (threads). Each water molecule requires two hydrogen 
 * atoms and one oxygen atom, and the threads must synchronize to ensure that molecules are formed correctly without 
 * incomplete or incorrect combinations. 
 * 
 * Hydrogen threads and oxygen threads arrive and must group together in sets of two hydrogens and one oxygen 
 * to form an H₂O molecule.
 * 
 * A barrier ensures that all three threads (2H + 1O) are ready before forming a molecule.
 * The process repeats, with threads synchronizing to avoid race conditions or incorrect groupings.
 * */

// Class to demonstrate the Building H2O Problem using semaphores and CyclicBarrier for synchronization
public class BuildingH2OProblem {
    // Constants
    private static final int NUM_HYDROGEN = 8; // Number of hydrogen threads
    private static final int NUM_OXYGEN = 4;   // Number of oxygen threads
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore hydrogenSem = new Semaphore(2); // Controls hydrogen thread access (2 per molecule)
    private static final Semaphore oxygenSem = new Semaphore(1);   // Controls oxygen thread access (1 per molecule)
    private static final Semaphore mutex = new Semaphore(1);       // Ensures mutual exclusion for shared state
    private static final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        // Action executed when 2 hydrogens and 1 oxygen reach the barrier
        System.out.println("H2O molecule formed!");
    });

    // Shared state
    private static int hydrogenCount = 0; // Tracks hydrogen threads waiting
    private static int oxygenCount = 0;   // Tracks oxygen threads waiting

    // Hydrogen thread: Represents a hydrogen atom trying to form an H2O molecule
    static class Hydrogen extends Thread {
        private final int id;

        public Hydrogen(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(1000));

                    // Try to join molecule formation
                    hydrogenSem.acquire(); // Ensure only 2 hydrogens at a time
                    mutex.acquire(); // Lock shared state
                    hydrogenCount++;
                    System.out.println("Hydrogen " + id + " is ready, " + hydrogenCount + " hydrogens waiting");
                    mutex.release(); // Unlock shared state

                    // Wait at barrier for 2 hydrogens and 1 oxygen
                    bond();
                    barrier.await();

                    // After molecule formation, reset for next round
                    mutex.acquire(); // Lock shared state
                    hydrogenCount--;
                    System.out.println("Hydrogen " + id + " bonded, " + hydrogenCount + " hydrogens remaining");
                    if (hydrogenCount == 0 && oxygenCount == 0) {
                        // Reset semaphores for next molecule
                        hydrogenSem.release(2);
                        oxygenSem.release();
                    }
                    mutex.release(); // Unlock shared state
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void bond() {
            System.out.println("Hydrogen " + id + " is bonding...");
        }
    }

    // Oxygen thread: Represents an oxygen atom trying to form an H2O molecule
    static class Oxygen extends Thread {
        private final int id;

        public Oxygen(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(1000));

                    // Try to join molecule formation
                    oxygenSem.acquire(); // Ensure only 1 oxygen at a time
                    mutex.acquire(); // Lock shared state
                    oxygenCount++;
                    System.out.println("Oxygen " + id + " is ready, " + oxygenCount + " oxygens waiting");
                    mutex.release(); // Unlock shared state

                    // Wait at barrier for 2 hydrogens and 1 oxygen
                    bond();
                    barrier.await();

                    // After molecule formation, reset for next round
                    mutex.acquire(); // Lock shared state
                    oxygenCount--;
                    System.out.println("Oxygen " + id + " bonded, " + oxygenCount + " oxygens remaining");
                    if (hydrogenCount == 0 && oxygenCount == 0) {
                        // Reset semaphores for next molecule
                        hydrogenSem.release(2);
                        oxygenSem.release();
                    }
                    mutex.release(); // Unlock shared state
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void bond() {
            System.out.println("Oxygen " + id + " is bonding...");
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Building H2O Problem Simulation...");

        // Start hydrogen threads
        Hydrogen[] hydrogens = new Hydrogen[NUM_HYDROGEN];
        for (int i = 0; i < NUM_HYDROGEN; i++) {
            hydrogens[i] = new Hydrogen(i + 1);
            hydrogens[i].start();
        }

        // Start oxygen threads
        Oxygen[] oxygens = new Oxygen[NUM_OXYGEN];
        for (int i = 0; i < NUM_OXYGEN; i++) {
            oxygens[i] = new Oxygen(i + 1);
            oxygens[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(10000); // Run for 10 seconds
            for (Hydrogen h : hydrogens) {
                h.interrupt();
            }
            for (Oxygen o : oxygens) {
                o.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}