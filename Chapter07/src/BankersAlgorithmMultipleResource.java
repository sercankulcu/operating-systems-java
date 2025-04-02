/*
 * This Java program implements the Banker's Algorithm for deadlock avoidance with multiple resources.
 * The algorithm ensures a system remains in a safe state by checking if resource allocation can
 * complete all processes without leading to a deadlock. It uses matrices for maximum need,
 * current allocation, and available resources.
 */
public class BankersAlgorithmMultipleResource {
    // Static variables to hold system state
    static int resources;       // Number of resource types
    static int processes;       // Number of processes
    static int[] available;     // Available instances of each resource
    static int[][] max;         // Maximum resource demand for each process
    static int[][] allocation;  // Currently allocated resources for each process
    static int[][] need;        // Remaining resource needs for each process (max - allocation)

    /*
     * Initializes the system with sample data for processes and resources.
     * Sets up available resources, maximum needs, current allocations, and calculates needs.
     */
    static void inputData() {
        // Define system parameters
        processes = 5;  // 5 processes (P0 to P4)
        resources = 4;  // 4 resource types (e.g., A, B, C, D)
        
        // Initialize arrays
        available = new int[resources];      // Resources currently available
        max = new int[processes][resources]; // Max resources each process may request
        allocation = new int[processes][resources]; // Resources currently allocated
        need = new int[processes][resources]; // Resources still needed
        
        // Set initial available resources
        available[0] = 3;  // Resource A: 5 instances
        //available[0] = 2; // make system unsafe
        available[1] = 3;  // Resource B: 3 instances
        available[2] = 2;  // Resource C: 2 instances
        available[3] = 2;  // Resource D: 2 instances

        // Set maximum resource demands for each process
        max[0][0] = 4; max[0][1] = 1; max[0][2] = 1; max[0][3] = 1; // P0 max needs
        max[1][1] = 2; max[1][2] = 1; max[1][3] = 2;               // P1 max needs
        max[2][0] = 4; max[2][1] = 2; max[2][2] = 1;               // P2 max needs
        max[3][0] = 1; max[3][1] = 1; max[3][2] = 1; max[3][3] = 1; // P3 max needs
        max[4][0] = 2; max[4][1] = 1; max[4][2] = 1;               // P4 max needs

        // Set current resource allocations for each process
        allocation[0][0] = 3; allocation[0][2] = 1; allocation[0][3] = 1; // P0 allocation
        allocation[1][1] = 1;                                            // P1 allocation
        allocation[2][0] = 1; allocation[2][1] = 1; allocation[2][2] = 1; // P2 allocation
        allocation[3][0] = 1; allocation[3][1] = 1; allocation[3][3] = 1; // P3 allocation
        // P4 has no allocation yet

        // Calculate need matrix (need = max - allocation)
        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    /*
     * Checks if a process's current need can be satisfied with available resources.
     * Returns true if all resource needs are less than or equal to available resources.
     */
    static boolean check(int process) {
        for (int i = 0; i < resources; i++) {
            if (need[process][i] > available[i]) {
                // If any resource need exceeds availability, process cannot run
                return false;
            }
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
        int[] work = new int[resources]; // Working copy of available resources
        
        // Initialize work array with current available resources
        for (int i = 0; i < resources; i++) {
            work[i] = available[i];
        }
        
        // Continue until all processes are finished or no safe sequence is found
        while (count < processes) {
            boolean found = false; // Flag to check if any process can run in this iteration
            for (int i = 0; i < processes; i++) {
                if (!finished[i] && check(i)) {
                    // If process i can run, allocate its resources back to work
                    for (int j = 0; j < resources; j++) {
                        work[j] += allocation[i][j]; // Release allocated resources
                    }
                    finished[i] = true; // Mark process as completed
                    count++;
                    found = true; // Found a process that can run
                    System.out.println("Process " + i + " completed.");
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