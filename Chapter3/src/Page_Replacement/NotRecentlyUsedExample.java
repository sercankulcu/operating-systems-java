package Page_Replacement;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates the NRU page replacement algorithm:
 * 
 * Not Recently Used (NRU) is a page replacement algorithm that is used to select which page to 
 * replace when a page fault occurs and there are no free pages available. The NRU algorithm 
 * divides pages into two classes: "referenced" and "not referenced". Pages that have been 
 * accessed recently are marked as "referenced", and pages that have not been accessed recently 
 * are marked as "not referenced". When a page fault occurs, the NRU algorithm selects the 
 * oldest "not referenced" page for replacement.
 * 
 * */

public class NotRecentlyUsedExample {
    private static final int NUM_PAGES = 4;
    private static final int NUM_FRAMES = 2;

    public static void main(String[] args) {
        // Create a queue to store the pages in memory
        Queue<Page> pages = new LinkedList<>();
        // Create an array of pages to simulate page requests
        Page[] pageRequests = {
                new Page(1, false),
                new Page(2, false),
                new Page(3, true),
                new Page(4, false),
                new Page(1, true),
                new Page(2, true),
                new Page(5, false),
                new Page(6, false)
        };
        // Process the page requests
        for (Page page : pageRequests) {
            // Check if the page is already in memory
            boolean found = false;
            for (Page p : pages) {
                if (p.number == page.number) {
                    found = true;
                    p.referenced = page.referenced;
                    break;
                }
            }
            if (!found) {
                // The page is not in memory, so we need to bring it in
                if (pages.size() == NUM_FRAMES) {
                    // The page table is full, so we need to replace a page
                    // Find the oldest "not referenced" page to replace
                    Page toReplace = null;
                    for (Page p : pages) {
                        if (!p.referenced) {
                            toReplace = p;
                            break;
                        }
                    }
                    if (toReplace == null) {
                        // All pages are "referenced", so we replace the oldest page
                        toReplace = pages.poll();
                    }
                    pages.remove(toReplace);
                }
                // Add the new page to the page table
                pages.add(page);
            }
            // Print the state of the page table
            System.out.print("Page table: ");
            for (Page p : pages) {
                System.out.print(p.number + " (" + (p.referenced ? "R" : "NR") + ") ");
            }
            System.out.println();
        }
    }

    private static class Page {
        int number;
        boolean referenced;

        Page(int number, boolean referenced) {
            this.number = number;
            this.referenced = referenced;
        }
    }
}
