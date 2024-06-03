package arm_scheduling;

import java.util.Arrays;

/*
 * Here's an example Java code that implements LOOK Disk Arm Scheduling:
 * 
 * This code implements the LOOK disk arm scheduling algorithm by sorting the disk requests, 
 * keeping track of the current position of the disk arm, the total disk arm movement, and the 
 * direction in which the disk arm is moving, and processing the disk requests. The disk arm 
 * moves from the current position to the closest disk request in the current direction, and 
 * the total disk arm movement is updated with the absolute difference between the current and 
 * requested positions. If the disk arm has processed all disk requests in one direction, it 
 * changes direction, and continues processing disk requests until all disk requests have been 
 * processed. The total disk arm movement is returned as the result.
 * 
 * */

public class LOOKDiskArmScheduling {

	/*
	 * This function calculates the total movement of the disk arm
	 * to service a given list of requests in sorted order.
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

	  // Initialize the direction of movement
	  boolean direction = false;

	  // Iterate over all the requests
	  for (int i = 0; i < requests.length; i++) {

	    // If the direction is true, move the disk arm from the current position to the end
	    if (direction) {

	      // Print the current position and the next request
	      System.out.println(currentPosition + " -> " + requests[end]);

	      // Add the movement to the total movement
	      totalMovement += Math.abs(requests[end] - currentPosition);

	      // Update the current position to the next request
	      currentPosition = requests[end];

	      // Decrement the end index
	      end--;

	      // If the end index is less than 0, change the direction to false
	      if (end < 0) {
	        direction = false;
	      }
	    } else {

	      // If the direction is false, move the disk arm from the current position to the start
	      System.out.println(currentPosition + " -> " + requests[start]);

	      // Add the movement to the total movement
	      totalMovement += Math.abs(requests[start] - currentPosition);

	      // Update the current position to the next request
	      currentPosition = requests[start];

	      // Increment the start index
	      start++;

	      // If the start index is equal to the length of the requests, change the direction to true
	      if (start == requests.length) {
	        direction = true;
	      }
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
