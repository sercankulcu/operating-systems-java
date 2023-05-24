package Arm_Scheduling;

/*
 * Here's an example Java code that implements First-Come, First-Served (FCFS) Disk Arm Scheduling:
 * 
 * This code implements the FCFS disk arm scheduling algorithm by keeping track of the current 
 * position of the disk arm and the total disk arm movement. For each disk request, the disk 
 * arm moves from its current position to the requested disk track, and the total disk arm 
 * movement is updated with the absolute difference between the current and requested positions. 
 * The total disk arm movement is returned as the result.
 * 
 * */

public class FCFSDiskArmScheduling {
	
    static int diskArmMovement(int requests[], int head) {
    	
        int currentPosition = head;
        int totalMovement = 0;
        for (int i = 0; i < requests.length; i++) {
        	System.out.println(currentPosition + " -> " + requests[i]);
            totalMovement += Math.abs(currentPosition - requests[i]);
            currentPosition = requests[i];
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
