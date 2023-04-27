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
                totalMovement += currentPosition - requests[start];
                currentPosition = requests[start];
                start++;
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
