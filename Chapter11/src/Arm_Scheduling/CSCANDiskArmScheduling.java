package Arm_Scheduling;

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
	
	static int diskArmMovement(int requests[], int head) {
		
		int totalMovement = 0;
		Arrays.sort(requests);
		int currentPosition = head;
		int start = 0;
		int end = requests.length - 1;
		int temp = 0;
		int direction = 1; // -1:left 1:right
		int target = 0;

		for(int i = 0; head > requests[i]; i++) {
			temp = i;
		}

		if(direction == 1) {
			target = end;
			start = temp + 1;
		} else {
			target = 0;
			start = temp;
		}

		for(int i = 0; i < requests.length; i++) {

			System.out.println(currentPosition + " -> " + requests[start]);
			totalMovement += Math.abs(currentPosition - requests[start]);
			currentPosition = requests[start];

			if(start == target) { // arrived to target, change direction
				if(direction == -1) {
					start = end;
				} else {
					start = 0;
				}
			} else {
				start += direction;
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
