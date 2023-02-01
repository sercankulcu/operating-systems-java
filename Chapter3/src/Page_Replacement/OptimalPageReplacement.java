package Page_Replacement;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the Optimal Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Optimal Page Replacement algorithm by keeping track of the frames 
 * in an ArrayList, checking if a page is already in the frames, and if not, finding the page 
 * that will be used farthest in the future and replacing it with the current page. The number 
 * of page faults is returned as the result.
 * */

public class OptimalPageReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        ArrayList<Integer> frames = new ArrayList<>(capacity);
        int pageFaults = 0;
        for (int i = 0; i < n; i++) {
            if (frames.size() < capacity) {
                if (!frames.contains(pages[i])) {
                    frames.add(pages[i]);
                    pageFaults++;
                }
            } else {
                if (!frames.contains(pages[i])) {
                    int j = 0;
                    int farthest = i;
                    for (int k = 0; k < capacity; k++) {
                        int l;
                        for (l = i; l < n; l++) {
                            if (frames.get(k) == pages[l]) {
                                break;
                            }
                        }
                        if (l > farthest) {
                            farthest = l;
                            j = k;
                        }
                    }
                    frames.set(j, pages[i]);
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

