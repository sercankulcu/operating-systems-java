package Page_Replacement;

import java.util.LinkedList;
import java.util.Scanner;

/*
 * Here's an example Java code that implements the Second Chance Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Second Chance Page Replacement algorithm by keeping track of the 
 * frames in a linked list, checking if a page is already in the frames, and if not, iteratively 
 * removing the oldest page until a page without a reference bit is found and adding the current 
 * page to the end of the list. If a page is found in the frames, its reference bit is set to 
 * true. The number of page faults is returned as the result.
 * 
 * */

public class SecondChancePageReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        LinkedList<Page> frames = new LinkedList<>();
        int pageFaults = 0;
        for (int i = 0; i < n; i++) {
            if (frames.size() < capacity) {
                if (!containsPage(frames, pages[i])) {
                    Page page = new Page(pages[i]);
                    frames.addLast(page);
                    pageFaults++;
                } else {
                    Page page = getPage(frames, pages[i]);
                    page.referenceBit = true;
                }
            } else {
                while (true) {
                    Page page = frames.removeFirst();
                    if (page.referenceBit) {
                        page.referenceBit = false;
                        frames.addLast(page);
                    } else {
                        page = new Page(pages[i]);
                        frames.addLast(page);
                        pageFaults++;
                        break;
                    }
                }
            }
        }
        return pageFaults;
    }

    private static boolean containsPage(LinkedList<Page> frames, int pageNumber) {
        for (Page page : frames) {
            if (page.pageNumber == pageNumber) {
                return true;
            }
        }
        return false;
    }

    private static Page getPage(LinkedList<Page> frames, int pageNumber) {
        for (Page page : frames) {
            if (page.pageNumber == pageNumber) {
                return page;
            }
        }
        return null;
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

class Page {
    int pageNumber;
    boolean referenceBit;

    Page(int pageNumber) {
        this.pageNumber = pageNumber;
        referenceBit = true;
    }
}
