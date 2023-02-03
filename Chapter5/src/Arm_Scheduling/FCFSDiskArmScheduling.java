package Arm_Scheduling;

import java.util.Scanner;

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
    static int diskArmMovement(int requests[], int n) {
        int currentPosition = 11;
        int totalMovement = 0;
        for (int i = 0; i < n; i++) {
            totalMovement += Math.abs(currentPosition - requests[i]);
            currentPosition = requests[i];
        }
        return totalMovement;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of disk requests:");
        int n = sc.nextInt();
        int requests[] = new int[n];
        System.out.println("Enter the disk requests:");
        for (int i = 0; i < n; i++) {
            requests[i] = sc.nextInt();
        }
        int totalMovement = diskArmMovement(requests, n);
        System.out.println("Total disk arm movement: " + totalMovement);
    }
}
