package Page_Replacement;

import java.util.LinkedList;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the First-In-First-Out (FIFO) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the FIFO Page Replacement algorithm by keeping track of the frames in a 
 * linked list, checking if a page is already in the frames, and if not, removing the oldest 
 * page and adding the current page to the end of the list. The number of page faults is 
 * returned as the result.
 * */

public class FIFOPageReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        LinkedList<Integer> frames = new LinkedList<>();
        int pageFaults = 0;
        for (int i = 0; i < n; i++) {
            if (frames.size() < capacity) {
                if (!frames.contains(pages[i])) {
                    frames.addLast(pages[i]);
                    pageFaults++;
                }
            } else {
                if (!frames.contains(pages[i])) {
                    frames.removeFirst();
                    frames.addLast(pages[i]);
                    pageFaults++;
                }
            }
        }
        return pageFaults;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of pages:");
        int n = sc.nextInt();
        int pages[] = new int[n];
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }
        System.out.println("Enter number of frames:");
        int capacity = sc.nextInt();
        int pageFaults = pageFaults(pages, n, capacity);
        System.out.println("Number of page faults: " + pageFaults);
    }
}
