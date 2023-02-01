package Page_Replacement;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the Working Set Clock Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Working Set Clock algorithm by using an ArrayList to keep track of 
 * the frames and a pointer to determine which frame to replace next. When a page is requested 
 * and is not found in the frames, the page is added to the frames if there is still capacity, 
 * and if not, the pointer is used to determine which frame to replace next. The algorithm uses 
 * a clock approach, where the frames are marked with a -1 if they haven't been used recently. 
 * The pointer is incremented until a frame is found that has a -1, which indicates that it is 
 * a candidate for replacement. The number of page faults is tracked and returned as the result.
 * 
 * */

public class WorkingSetClockReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        ArrayList<Integer> frames = new ArrayList<>(capacity);
        int pageFaults = 0;
        int pointer = 0;
        for (int i = 0; i < n; i++) {
            if (!frames.contains(pages[i])) {
                if (frames.size() < capacity) {
                    frames.add(pages[i]);
                } else {
                    while (true) {
                        int j = pointer % capacity;
                        if (frames.get(j) == -1) {
                            frames.set(j, pages[i]);
                            pointer++;
                            break;
                        } else {
                            frames.set(j, -1);
                            pointer++;
                        }
                    }
                }
                pageFaults++;
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
