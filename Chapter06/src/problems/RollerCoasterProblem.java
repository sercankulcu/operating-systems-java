package problems;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/*
 * The Roller Coaster Problem is a synchronization problem in operating systems that models the coordination of 
 * passengers and a roller coaster car with limited capacity. The problem involves:
 * 
 * Passengers: Arrive and wait to board the roller coaster.
 * Car: Has a fixed capacity (e.g., 4 seats) and can only run when full. After each ride, it unloads all passengers 
 * and waits for a new full load.
 * The car and passengers must synchronize to ensure:
 * The car only runs when exactly full.
 * Passengers board only when the car is ready and has space.
 * All passengers unload before the next group boards.
 * No race conditions or deadlocks occur.
 * 
 * */

// Class to demonstrate the Roller Coaster Problem using semaphores and CyclicBarrier for synchronization
public class RollerCoasterProblem {
    // Constants
    private static final int CAR_CAPACITY = 4; // Number of seats in the car
    private static final int NUM_PASSENGERS = 12; // Number of passenger threads
    private static final Random random = new Random(); // Random number generator for delays

    // Synchronization primitives
    private static final Semaphore mutex = new Semaphore(1); // Ensures mutual exclusion for shared state
    private static final Semaphore boardQueue = new Semaphore(0); // Signals passengers to board
    private static final Semaphore unloadQueue = new Semaphore(0); // Signals passengers to unload
    private static final Semaphore carReady = new Semaphore(0); // Signals car to run
    private static final CyclicBarrier barrier = new CyclicBarrier(CAR_CAPACITY, () -> {
        // Action executed when the car is full (all passengers boarded)
        System.out.println("Car is full and starting the ride!");
    });

    // Shared state
    private static int waitingPassengers = 0; // Number of passengers waiting to board
    private static int boardedPassengers = 0; // Number of passengers currently boarded

    // Car thread: Manages boarding, running, and unloading
    static class Car extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // Wait for enough passengers to fill the car
                    System.out.println("Car is waiting for passengers...");
                    mutex.acquire(); // Lock shared state
                    if (waitingPassengers >= CAR_CAPACITY) {
                        // Allow passengers to board
                        boardQueue.release(CAR_CAPACITY);
                        waitingPassengers -= CAR_CAPACITY;
                        boardedPassengers = CAR_CAPACITY;
                        System.out.println("Car signals " + CAR_CAPACITY + " passengers to board");
                    }
                    mutex.release(); // Unlock shared state

                    // Wait for all passengers to board
                    carReady.acquire();

                    // Run the ride
                    runRide();

                    // Unload passengers
                    unloadQueue.release(CAR_CAPACITY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void runRide() throws InterruptedException {
            System.out.println("Car is running the ride...");
            Thread.sleep(random.nextInt(1000)); // Simulate ride duration (0-1 second)
            System.out.println("Ride finished!");
        }
    }

    // Passenger thread: Represents a passenger trying to ride the roller coaster
    static class Passenger extends Thread {
        private final int id;

        public Passenger(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Simulate arrival time
                    Thread.sleep(random.nextInt(2000));

                    // Try to board the car
                    mutex.acquire(); // Lock shared state
                    waitingPassengers++;
                    System.out.println("Passenger " + id + " arrives, " + waitingPassengers + " waiting");
                    mutex.release(); // Unlock shared state

                    // Wait for car to allow boarding
                    boardQueue.acquire();
                    board();

                    // Signal car when boarded
                    mutex.acquire(); // Lock shared state
                    boardedPassengers--;
                    System.out.println("Passenger " + id + " boarded, " + boardedPassengers + " still boarding");
                    if (boardedPassengers == 0) {
                        // All passengers boarded, signal car to run
                        carReady.release();
                    }
                    mutex.release(); // Unlock shared state

                    // Wait for ride to complete
                    barrier.await();

                    // Wait for unload signal
                    unloadQueue.acquire();
                    unload();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void board() {
            System.out.println("Passenger " + id + " is boarding the car...");
        }

        private void unload() {
            System.out.println("Passenger " + id + " is unloading from the car...");
        }
    }

    // Main method to set up and run the simulation
    public static void main(String[] args) {
        System.out.println("Starting Roller Coaster Problem Simulation...");

        // Start car thread
        Car car = new Car();
        car.start();

        // Start passenger threads
        Passenger[] passengers = new Passenger[NUM_PASSENGERS];
        for (int i = 0; i < NUM_PASSENGERS; i++) {
            passengers[i] = new Passenger(i + 1);
            passengers[i].start();
        }

        // Run for a fixed duration, then interrupt threads
        try {
            Thread.sleep(15000); // Run for 15 seconds
            car.interrupt();
            for (Passenger p : passengers) {
                p.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation ended.");
    }
}