package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Sushi Bar Problem is a synchronization problem in operating systems that models a small sushi bar with limited 
 * seating capacity, where customers arrive to eat and must be coordinated to ensure proper access to the shared resource 
 * (seats). The problem, as described in resources like the University of Wisconsin's notes, involves the following constraints:
 * 
 * The sushi bar has a fixed number of seats (e.g., 5).
 * Customers arrive randomly and can sit if there are enough empty seats.
 * If the bar is full, arriving customers leave immediately without waiting.
 * Customers eat for a while and then leave, freeing their seats.
 * The sushi bar operates in a first-come, first-served manner, and synchronization is needed to prevent race 
 * conditions when multiple customers try to sit or leave simultaneously.
 * 
 * */

// Class to demonstrate the Sushi Bar Problem using semaphores for synchronization
public class SushiBarProblem {
    // Constants
    private static final int NUM_SEATS = 5; // Number of seats in the sushi bar
    private static final int NUM_CUSTOMERS = 15; // Number of customer threads
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore seats = new Semaphore(NUM_SEATS); // Controls access to seats
    private static final Semaphore mutex = new Semaphore(1); // Protects shared state
    private static int currentCustomers = 0; // Tracks number of customers currently seated

    // Customer thread: Represents a customer trying to eat at the sushi bar
    static class Customer extends Thread {
        private final int id;

        public Customer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Simulate arrival time
                Thread.sleep(random.nextInt(2000));

                // Try to get a seat
                if (seats.tryAcquire()) {
                    // Seat acquired
                    mutex.acquire(); // Protect shared state
                    currentCustomers++;
                    System.out.println("Customer " + id + " sits down, " + currentCustomers + " customers seated");
                    mutex.release(); // Unlock shared state

                    // Eat
                    eat();

                    // Leave
                    mutex.acquire(); // Protect shared state
                    currentCustomers--;
                    System.out.println("Customer " + id + " leaves, " + currentCustomers + " customers seated");
                    mutex.release(); // Unlock shared state
                    seats.release(); // Free the seat
                } else {
                    // No seats available, customer leaves
                    System.out.println("Customer " + id + " finds bar full and leaves");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void eat() throws InterruptedException {
            System.out.println("Customer " + id + " is eating...");
            Thread.sleep(random.nextInt(3000)); // Simulate eating time (0-3 seconds)
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Sushi Bar Problem Simulation...");

        // Start customer threads
        Customer[] customers = new Customer[NUM_CUSTOMERS];
        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            customers[i] = new Customer(i + 1);
            customers[i].start();
        }

        // Run for a fixed duration, then wait for threads to complete
        try {
            Thread.sleep(10000); // Run for 10 seconds
            for (Customer c : customers) {
                c.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}