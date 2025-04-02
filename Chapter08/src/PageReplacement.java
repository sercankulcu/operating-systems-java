import java.util.LinkedList;
import java.util.Queue;

/*
 * Here's an implementation of the Page Replacement algorithm in Java, specifically the 
 * FIFO (First-In, First-Out) algorithm:
 * 
 * This code implements the FIFO Page Replacement algorithm by maintaining a queue of Page 
 * objects, each with a page number. The main method simulates page requests by looping through 
 * an array of page numbers (representing the reference string) and checking if the page is 
 * already in the queue. If the page is not in the queue and the queue is not full, the page is 
 * added to the queue. If the page is not in the queue and the queue is full, the oldest page 
 * in the queue (the one that was added first) is removed and the new page is added. The total 
 * number of page faults is tracked and displayed at the end of the simulation.
 * 
 * Key components:
 * - Page class to represent memory pages
 * - Queue to maintain FIFO order
 * - Reference string to simulate page requests
 * - Page fault counter to track performance
 * */

public class PageReplacement {
   // Inner class to represent a page in memory with a page number
   static class Page {
      int pageNumber;

      // Constructor to initialize a page with its number
      public Page(int pageNumber) {
         this.pageNumber = pageNumber;
      }
   }

   public static void main(String[] args) {
      // Define the number of page frames available in memory
      int pageCount = 4;
      
      // Reference string representing sequence of page requests
      int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
      
      // Queue to store pages in memory, following FIFO order
      Queue<Page> pageQueue = new LinkedList<>();

      // Counter for page faults (when a requested page isn't in memory)
      int pageFaults = 0;
      
      // Iterate through each page request in the reference string
      for (int i = 0; i < referenceString.length; i++) {
         int pageNumber = referenceString[i]; // Current page being requested

         // Case 1: Memory has space available (queue not full)
         if (pageQueue.size() < pageCount) {
            boolean pageExists = false;
            // Check if the requested page is already in memory
            for (Page page : pageQueue) {
               if (page.pageNumber == pageNumber) {
                  pageExists = true;
                  break; // Exit loop if page is found
               }
            }
            // If page isn't in memory, add it and increment page faults
            if (!pageExists) {
               pageQueue.add(new Page(pageNumber));
               pageFaults++;
            }
         } 
         // Case 2: Memory is full (queue at capacity)
         else {
            boolean pageExists = false;
            // Check if the requested page is already in memory
            for (Page page : pageQueue) {
               if (page.pageNumber == pageNumber) {
                  pageExists = true;
                  break; // Exit loop if page is found
               }
            }
            // If page isn't in memory, replace oldest page (FIFO)
            if (!pageExists) {
               pageQueue.remove(); // Remove the oldest page (front of queue)
               pageQueue.add(new Page(pageNumber)); // Add new page to end
               pageFaults++; // Increment page fault counter
            }
         }

         // Display current contents of memory frames
         System.out.print("Pages in Frame: ");
         for (Page page : pageQueue) {
            System.out.print(page.pageNumber + " ");
         }
         System.out.println(); // New line after displaying frame contents
      }

      // Print total number of page faults after simulation completes
      System.out.println("Total Page Faults: " + pageFaults);
   }
}