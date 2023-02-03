package Arm_Scheduling;

import java.util.Arrays;
import java.util.Scanner;

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
    static int diskArmMovement(int requests[], int n, int initialPosition) {
        int totalMovement = 0;
        Arrays.sort(requests);
        int currentPosition = initialPosition;
        int start = 0;
        int end = n - 1;
        while (start <= end) {
            if (currentPosition <= requests[end]) {
                totalMovement += requests[end] - currentPosition;
                currentPosition = requests[end];
                end--;
            } else {
                totalMovement += currentPosition - requests[0] + requests[end] - requests[0] + 1;
                currentPosition = requests[0];
                start = n - 1;
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
