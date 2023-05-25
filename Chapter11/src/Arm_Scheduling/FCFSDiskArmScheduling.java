package Arm_Scheduling;

/*
 * This code implements the First-Come, First-Served (FCFS) Disk Arm Scheduling algorithm.
 *
 * The algorithm works by keeping track of the current position of the disk arm and the total disk arm movement.
 * For each disk request, the disk arm moves from its current position to the requested disk track,
 * and the total disk arm movement is updated with the absolute difference between the current and requested positions.
 * The total disk arm movement is returned as the result.
 */

public class FCFSDiskArmScheduling {

    static int diskArmMovement(int[] requests, int head) {
        // Get the current position of the disk arm.
        int currentPosition = head;

        // Initialize the total disk arm movement to 0.
        int totalMovement = 0;

        // Iterate over all the disk requests.
        for (int i = 0; i < requests.length; i++) {
            // Print the current position and the requested position.
            System.out.println(currentPosition + " -> " + requests[i]);

            // Calculate the absolute difference between the current and requested positions.
            int movement = Math.abs(currentPosition - requests[i]);

            // Update the total disk arm movement.
            totalMovement += movement;

            // Set the current position to the requested position.
            currentPosition = requests[i];
        }

        // Return the total disk arm movement.
        return totalMovement;
    }

    public static void main(String[] args) {
        // Create an array of disk requests.
        int[] requests = {98, 183, 37, 122, 14, 124, 65, 67};

        // Get the current position of the disk arm.
        int head = 53;

        // Calculate the total disk arm movement.
        int totalMovement = diskArmMovement(requests, head);

        // Print the total disk arm movement.
        System.out.println("Total disk arm movement: " + totalMovement);
    }
}