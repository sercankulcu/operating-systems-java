package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Barber Shop Problem is a classic synchronization problem in operating systems that illustrates concurrency 
 * control, process coordination, and resource management. The problem involves a barber shop with one barber, 
 * a limited number of waiting chairs, and customers who arrive to get haircuts. The barber cuts hair when customers 
 * are present and sleeps when there are none. Customers wait if there are available chairs, or leave if the waiting 
 * room is full. This problem demonstrates key operating system concepts such as mutual exclusion, condition 
 * synchronization, and avoiding deadlock or starvation.
 * 
 * */
// Class to demonstrate the Barber Shop Problem using semaphores for synchronization
public class BarberShopProblem {
    // Constants
    private static final int CHAIRS = 5; // Number of waiting chairs
    private static final Random random = new Random(); // Random number generator for delays

    // Semaphores for synchronization
    private static final Semaphore customers = new Semaphore(0); // Signals waiting customers
    private static final Semaphore barber = new Semaphore(0); // Signals barber availability
    private static final Semaphore mutex = new Semaphore(1); // Ensures mutual exclusion for shared state
    private static final Semaphore waitingRoom = new Semaphore(CHAIRS); // Controls waiting room capacity

    // Shared state
    private static int waitingCustomers = 0; // Number of customers in waiting room

    // Barber thread: Cuts hair or sleeps based on customer availability
    static class Barber extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // Wait for a customer
                    customers.acquire();
                    mutex.acquire(); // Lock shared state
                    waitingCustomers--; // Customer moves from waiting room to barber chair
                    waitingRoom.release(); // Free a waiting chair
                    System.out.println("Barber is ready, " + waitingCustomers + " customers waiting");
                    mutex.release(); // Unlock shared state

                    // Signal customer to get haircut
                    barber.release();

                    // Cut hair
                    cutHair();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void cutHair() throws InterruptedException {
            System.out.println("Barber is cutting hair...");
            Thread.sleep(random.nextInt(2000)); // Simulate haircut time (0-2 seconds)
            System.out.println("Barber finished cutting hair");
        }
    }

    // Customer thread: Arrives to get a haircut or leaves if no chairs are available
    static class Customer extends Thread {
        private final int id;

        public Customer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Try to enter waiting room
                if (waitingRoom.tryAcquire()) { // Check if a chair is available
                    mutex.acquire(); // Lock shared state
                    waitingCustomers++; // Occupy a waiting chair
                    System.out.println("Customer " + id + " enters waiting room, " + waitingCustomers + " waiting");
                    mutex.release(); // Unlock shared state

                    // Signal barber that a customer is waiting
                    customers.release();

                    // Wait for barber to be ready
                    barber.acquire();

                    // Get haircut
                    getHaircut();
                } else {
                    // No chairs available, customer leaves
                    System.out.println("Customer " + id + " finds waiting room full and leaves");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void getHaircut() throws InterruptedException {
            System.out.println("Customer " + id + " is getting a haircut");
            Thread.sleep(random.nextInt(500)); // Simulate time in barber chair
            System.out.println("Customer " + id + " leaves after haircut");
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Barber Shop Problem Simulation...");

        // Start barber thread
        Barber barber = new Barber();
        barber.start();

        // Generate customers over time
        int customerId = 1;
        try {
            for (int i = 0; i < 20; i++) { // Simulate 20 customers
                Customer customer = new Customer(customerId++);
                customer.start();
                Thread.sleep(random.nextInt(1000)); // Random arrival interval (0-1 second)
            }

            // Allow simulation to complete
            Thread.sleep(5000);

            // Interrupt barber to end simulation
            barber.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}