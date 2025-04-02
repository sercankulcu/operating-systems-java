/*
 * This Java program implements the Banker's Algorithm for deadlock avoidance with a single resource type.
 * The algorithm ensures the system remains in a safe state by determining if resource allocation
 * allows all processes to complete without deadlock. It uses arrays to track maximum need,
 * current allocation, and available resources for a single resource type.
 */
public class BankersAlgorithmSingleResource {
    // Static variables to hold system state
    static int processes;       // Number of processes
    static int available;       // Available instances of the single resource
    static int[] max;           // Maximum resource demand for each process
    static int[] allocation;    // Currently allocated resources for each process
    static int[] need;          // Remaining resource needs for each process (max - allocation)

    /*
     * Initializes the system with sample data for processes and a single resource.
     * Sets up available resources, maximum needs, current allocations, and calculates needs.
     */
    static void inputData() {
        // Define system parameters
        processes = 4;  // 4 processes (P0 to P3)
        
        // Initialize arrays
        max = new int[processes];        // Max resources each process may request
        allocation = new int[processes]; // Resources currently allocated
        need = new int[processes];       // Resources still needed
        
        // Set initial available resources
        available = 2;  // 2 instances of the single resource available

        // Set maximum resource demands for each process
        max[0] = 6;  // P0 max need: 6 units
        max[1] = 5;  // P1 max need: 5 units
        max[2] = 4;  // P2 max need: 4 units
        max[3] = 7;  // P3 max need: 7 units

        // Set current resource allocations for each process
        allocation[0] = 1;  // P0 allocated: 1 unit
        allocation[1] = 1;  // P1 allocated: 1 unit
        allocation[2] = 2;  // P2 allocated: 2 units
        //allocation[2] = 1; // make system unsafe
        allocation[3] = 2;  // P3 allocated: 4 units

        // Calculate need array (need = max - allocation)
        for (int i = 0; i < processes; i++) {
            need[i] = max[i] - allocation[i];
            // P0: 6-1=5, P1: 5-1=4, P2: 4-2=2, P3: 7-4=3
        }
    }

    /*
     * Checks if a process's current need can be satisfied with available resources.
     * Returns true if the need is less than or equal to available resources.
     */
    static boolean check(int process) {
        if (need[process] > available) {
            // If the process needs more than what's available, it cannot run
            return false;
        }
        return true; // Process can run with current available resources
    }

    /*
     * Implements the safety algorithm to determine if the system is in a safe state.
     * Simulates resource allocation to find a safe sequence where all processes can complete.
     * Returns true if a safe sequence exists, false otherwise.
     */
    static boolean safety() {
        int count = 0; // Number of finished processes
        boolean[] finished = new boolean[processes]; // Tracks completed processes
        
        // Continue until all processes are finished or no safe sequence is found
        while (count < processes) {
            boolean found = false; // Flag to check if any process can run in this iteration
            for (int i = 0; i < processes; i++) {
                if (!finished[i] && check(i)) {
                    // If process i can run, allocate its resources back to available
                    available += allocation[i]; // Release allocated resources
                    finished[i] = true; // Mark process as completed
                    count++;
                    found = true; // Found a process that can run
                    System.out.println("Process " + i + " completed, available now: " + available);
                }
            }
            if (!found) {
                // No process can run with current resources: unsafe state
                return false;
            }
        }
        return true; // All processes completed: safe state
    }

    /*
     * Main method to run the Banker's Algorithm.
     * Initializes data and checks if the system is in a safe state.
     */
    public static void main(String[] args) {
        // Initialize system data
        inputData();
        
        // Check system safety and print result
        if (safety()) {
            System.out.println("The system is in a safe state.");
        } else {
            System.out.println("The system is in an unsafe state.");
        }
    }
}