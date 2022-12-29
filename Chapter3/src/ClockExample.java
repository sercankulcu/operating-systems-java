import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that implements the Clock page replacement algorithm:
 * 
 * */

public class ClockExample {
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
                    // Search the queue for a page to replace
                    Page toReplace = null;
                    while (toReplace == null) {
                        Page p = pages.poll();
                        if (!p.referenced) {
                            toReplace = p;
                        } else {
                            p.referenced = false;
                            pages.add(p);
                        }
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
