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
		int direction = 0; // 0:left 1:right

		for(int i = 0; head > requests[i]; i++) {
			temp = i;
		}

		if(direction == 0) { // move to left
			// start from temp to 0, then go to the end, then iterate to temp
			start = temp;
			while(start >= 0) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += currentPosition - requests[start];
				currentPosition = requests[start];
				start--;
			}
			start = end;
			while(start > temp) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += requests[start] - currentPosition;
				currentPosition = requests[start];
				start--;
			}
		}
		if(direction == 1) { // move to left
			// start from temp to end, then go to the 0, then iterate to temp
			start = temp + 1;
			while(start <= end) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += requests[start] - currentPosition;
				currentPosition = requests[start];
				start++;
			}
			start = 0;
			while(start < temp) {
				System.out.println(currentPosition + " -> " + requests[start]);
				totalMovement += currentPosition - requests[start];
				currentPosition = requests[start];
				start++;
			}
		}
		
		/*while (start <= end) {
			if (currentPosition <= requests[end]) {
				System.out.println(currentPosition + " -> " + requests[end]);
				totalMovement += requests[end] - currentPosition;
				currentPosition = requests[end];
				end--;
			} else {
				System.out.println(currentPosition + " -> " + requests[0]);
				totalMovement += currentPosition - requests[0] + requests[end] - requests[0] + 1;
				currentPosition = requests[0];
				start = requests.length - 1;
			}
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
