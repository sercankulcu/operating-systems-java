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
	
	static int diskArmMovement(int requests[], int head) {
		
		int currentPosition = head;
		int totalMovement = 0;
		boolean visited[] = new boolean[requests.length];
		for (int i = 0; i < requests.length; i++) {
			int minDistance = Integer.MAX_VALUE;
			int nextRequest = -1;
			for (int j = 0; j < requests.length; j++) {
				
				if (!visited[j] && Math.abs(currentPosition - requests[j]) < minDistance) {
					minDistance = Math.abs(currentPosition - requests[j]);
					nextRequest = j;
				}
			}
			visited[nextRequest] = true;
			System.out.println(currentPosition + " -> " + requests[nextRequest]);
			totalMovement += Math.abs(currentPosition - requests[nextRequest]);
			currentPosition = requests[nextRequest];
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
