package Page_Replacement;

import java.util.LinkedHashMap;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the Least Recently Used (LRU) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the LRU algorithm by using a linked hash map to keep track of the frames, 
 * where the order of the keys is the order in which they were last accessed. When a page is 
 * requested and is not found in the frames, the page is added to the frames if there is still 
 * capacity, and if not, the first key (i.e., the least recently used page) is removed to make 
 * room for the new page. The number of page faults is tracked and returned as the result.
 * 
 * */

public class LRUPageReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        LinkedHashMap<Integer, Integer> frames = new LinkedHashMap<>(capacity, 1.0f, true);
        int pageFaults = 0;
        for (int i = 0; i < n; i++) {
            if (!frames.containsKey(pages[i])) {
                if (frames.size() == capacity) {
                    frames.remove(frames.keySet().iterator().next());
                }
                frames.put(pages[i], i);
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
