package Page_Replacement;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the Not Frequently Used (NFU) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Not Frequently Used (NFU) algorithm by using an ArrayList to keep 
 * track of the frames and an array to keep track of the number of page accesses for each page 
 * in the frames. When a page is requested and is not found in the frames, the page is added to 
 * the frames if there is still capacity, and if not, the page with the least number of accesses 
 * is replaced. The number of accesses for each page is incremented after each iteration, and 
 * the number of page faults is tracked and returned as the result.
 * 
 * */

public class NotFrequentlyUsedReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        ArrayList<Integer> frames = new ArrayList<>(capacity);
        int pageFaults = 0;
        int pageCounter[] = new int[n];
        for (int i = 0; i < n; i++) {
            if (!frames.contains(pages[i])) {
                if (frames.size() < capacity) {
                    frames.add(pages[i]);
                } else {
                    int minCounterIndex = 0;
                    for (int j = 0; j < frames.size(); j++) {
                        if (pageCounter[j] < pageCounter[minCounterIndex]) {
                            minCounterIndex = j;
                        }
                    }
                    frames.set(minCounterIndex, pages[i]);
                }
                pageFaults++;
            }
            for (int j = 0; j < frames.size(); j++) {
                if (frames.get(j) != pages[i]) {
                    pageCounter[j]++;
                } else {
                    pageCounter[j] = 0;
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
