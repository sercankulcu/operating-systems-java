package arm_scheduling;

import java.util.Arrays;

/*
 * Here's an example Java code that implements C-SCAN Disk Arm Scheduling:
 * 
 * This code implements the C-SCAN disk arm scheduling algorithm by sorting the disk requests, 
 * keeping track of the current position of the disk arm and the total disk arm movement, and 
 * processing the disk requests. The disk arm moves from the current position to the disk request 
 * with the closest position on the disk, and the total disk arm movement is updated with the 
 * absolute difference between the current and requested positions. If the disk arm has processed 
 * all disk requests on one side of the disk, it moves to the other end of the disk, and 
 * continues processing disk requests until all disk requests have been processed. The total 
 * disk arm movement is returned as the result.
 * 
 * */

public class CSCANDiskArmScheduling {

	/*
	 * This function calculates the total movement of the disk arm
	 * to service a given list of requests in sorted order, using the
	 * greedy algorithm.
	 */
	static int diskArmMovement(int[] requests, int head) {

		// Initialize the total movement
		int totalMovement = 0;

		// Sort the requests in ascending order
		Arrays.sort(requests);

		// Initialize the current position of the disk arm
		int currentPosition = head;

		// Initialize the start and end indices
		int start = 0;
		int end = requests.length - 1;

		// Initialize the temporary variable
		int temp = 0;

		// Initialize the direction of movement
		int direction = -1; // -1:left 1:right

		// Initialize the target index
		int target = 0;

		// Find the index of the current position in the sorted requests
		for (int i = 0; head > requests[i]; i++) {
			temp = i;
		}

		// If the direction is positive, set the target index to the end of the requests
		if (direction == 1) {
			target = end;
			start = temp + 1;
		} else {
			// If the direction is negative, set the target index to the beginning of the requests
			target = 0;
			start = temp;
		}

		// Iterate over all the requests
		for (int i = 0; i < requests.length; i++) {

			// Print the current position and the next request
			System.out.println(currentPosition + " -> " + requests[start]);

			// Add the movement to the total movement
			totalMovement += Math.abs(currentPosition - requests[start]);

			// Update the current position to the next request
			currentPosition = requests[start];

			// If the current index is equal to the target index, change the direction
			if (start == target) {
				if (direction == -1) {
					start = end;
				} else {
					start = 0;
				}
			} else {
				// Increment the start index by the direction
				start += direction;
			}
		}

		// Return the total movement
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
