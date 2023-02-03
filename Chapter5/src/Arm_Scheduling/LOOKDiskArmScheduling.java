package Arm_Scheduling;

import java.util.Arrays;
import java.util.Scanner;

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
    static int diskArmMovement(int requests[], int n, int initialPosition) {
        int totalMovement = 0;
        Arrays.sort(requests);
        int currentPosition = initialPosition;
        int start = 0;
        int end = n - 1;
        boolean direction = false;
        for (int i = 0; i < n; i++) {
            if (direction) {
                totalMovement += Math.abs(requests[end] - currentPosition);
                currentPosition = requests[end];
                end--;
                if (end < 0) {
                    direction = false;
                }
            } else {
                totalMovement += Math.abs(requests[start] - currentPosition);
                currentPosition = requests[start];
                start++;
                if (start == n) {
                    direction = true;
                }
            }
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
        System.out.println("Enter initial disk arm position:");
        int initialPosition = sc.nextInt();
        int totalMovement = diskArmMovement(requests, n, initialPosition);
        System.out.println("Total disk arm movement: " + totalMovement);
    }
}
