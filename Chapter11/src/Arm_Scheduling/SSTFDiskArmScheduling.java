package Arm_Scheduling;

/*
 * Here's an example Java code that implements Shortest Seek Time First (SSTF) Disk Arm Scheduling:
 * 
 * This code implements the SSTF disk arm scheduling algorithm by keeping track of the current 
 * position of the disk arm and the total disk arm movement. For each disk request, the code 
 * calculates the distance to all the unprocessed disk requests and selects the one with the 
 * shortest distance. The disk arm moves to the selected disk request, and the total disk arm 
 * movement is updated with the absolute difference between the current and requested positions. 
 * The code keeps track of which disk requests have already been processed by using a boolean 
 * array. The total disk arm movement is returned as the result.
 * 
 * */

public class SSTFDiskArmScheduling {

	/*
	 * This function calculates the total movement of the disk arm
	 * to service a given list of requests.
	 */
	static int diskArmMovement(int requests[], int head) {

	  // Initialize the current position of the disk arm
	  int currentPosition = head;

	  // Initialize the total movement
	  int totalMovement = 0;

	  // Initialize a boolean array to keep track of which requests have been visited
	  boolean visited[] = new boolean[requests.length];

	  // Iterate over all the requests
	  for (int i = 0; i < requests.length; i++) {

	    // Find the request that is closest to the current position
	    int minDistance = Integer.MAX_VALUE;
	    int nextRequest = -1;
	    for (int j = 0; j < requests.length; j++) {

	      // If the request has not been visited and it is closer to the current position than the current closest request
	      if (!visited[j] && Math.abs(currentPosition - requests[j]) < minDistance) {

	        // Update the closest request
	        minDistance = Math.abs(currentPosition - requests[j]);
	        nextRequest = j;
	      }
	    }

	    // Mark the request as visited
	    visited[nextRequest] = true;

	    // Print the current position and the next request
	    System.out.println(currentPosition + " -> " + requests[nextRequest]);

	    // Add the movement to the total movement
	    totalMovement += Math.abs(currentPosition - requests[nextRequest]);

	    // Update the current position to the next request
	    currentPosition = requests[nextRequest];
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
