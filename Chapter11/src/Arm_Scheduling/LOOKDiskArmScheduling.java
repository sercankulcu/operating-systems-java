package Arm_Scheduling;

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

	static int diskArmMovement(int requests[], int head) {

		int totalMovement = 0;
		Arrays.sort(requests);
		int currentPosition = head;
		int start = 0;
		int end = requests.length - 1;
		boolean direction = false;
		for (int i = 0; i < requests.length; i++) {
			if (direction) {
				System.out.println(currentPosition + " -> " + requests[end]);
				totalMovement += Math.abs(requests[end] - currentPosition);
				currentPosition = requests[end];
				end--;
				if (end < 0) {
					direction = false;
				}
			} else {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += Math.abs(requests[start] - currentPosition);
				currentPosition = requests[start];
				start++;
				if (start == requests.length) {
					direction = true;
				}
			}
		}
		return totalMovement;
	}

	public static void main(String[] args) {

		int requests[] = {98, 183, 37, 122, 14, 124, 65, 67};
		int head = 53;
		int totalMovement = diskArmMovement(requests, head);
		System.out.println("Total disk arm movement: " + totalMovement);
	}
}
