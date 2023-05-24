package Arm_Scheduling;

import java.util.Arrays;
import java.util.Scanner;

/*
 * Here's an example Java code that implements SCAN Disk Arm Scheduling:
 * 
 * This code implements the SCAN disk arm scheduling algorithm by sorting the disk requests, 
 * keeping track of the current position of the disk arm and the total disk arm movement, and 
 * processing the disk requests from one end of the disk to the other. The disk arm moves from 
 * the current position to the disk request with the closest position on the disk, and the total 
 * disk arm movement is updated with the absolute difference between the current and requested 
 * positions. The total disk arm movement is returned as the result.
 * 
 * */

public class SCANDiskArmScheduling {

	static int diskArmMovement(int requests[], int head) {

		int totalMovement = 0;
		Arrays.sort(requests);
		int currentPosition = head;
		int start = 0;
		int end = requests.length - 1;
		int temp = 0;
		int direction = 1; // 0:left 1:right

		for(int i = 0; head > requests[i]; i++) {
			temp = i;
		}

		if(direction == 0) { // move to left
			// start from temp to 0, then go to the end
			start = temp;
			while(start >= 0) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += currentPosition - requests[start];
				currentPosition = requests[start];
				start--;
			}
			start = temp + 1;
			while(start <= end) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += requests[start] - currentPosition;
				currentPosition = requests[start];
				start++;
			}
		}
		if(direction == 1) { // move to left
			// start from temp to end, then go to 0
			start = temp + 1;
			while(start <= end) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += requests[start] - currentPosition;
				currentPosition = requests[start];
				start++;
			}
			start = temp;
			while(start >= 0) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += currentPosition - requests[start];
				currentPosition = requests[start];
				start--;
			}
		}
		/*
			if (currentPosition <= requests[end]) {
				System.out.println(currentPosition + " -> " + requests[end]);
				totalMovement += requests[end] - currentPosition;
				currentPosition = requests[end];
				end--;
			} else {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += currentPosition - requests[start];
				currentPosition = requests[start];
				start++;
			}*/
		return totalMovement;
	}

	public static void main(String[] args) {

		int requests[] = {98, 183, 37, 122, 14, 124, 65, 67};
		int head = 53;
		int totalMovement = diskArmMovement(requests, head);
		System.out.println("Total disk arm movement: " + totalMovement);
	}
}
