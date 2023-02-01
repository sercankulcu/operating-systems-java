

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
 * */

public class PageReplacement {
   static class Page {
      int pageNumber;

      public Page(int pageNumber) {
         this.pageNumber = pageNumber;
      }
   }

   public static void main(String[] args) {
      int pageCount = 4;
      int[] referenceString = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2};
      Queue<Page> pageQueue = new LinkedList<>();

      int pageFaults = 0;
      for (int i = 0; i < referenceString.length; i++) {
         int pageNumber = referenceString[i];

         if (pageQueue.size() < pageCount) {
            boolean pageExists = false;
            for (Page page : pageQueue) {
               if (page.pageNumber == pageNumber) {
                  pageExists = true;
                  break;
               }
            }
            if (!pageExists) {
               pageQueue.add(new Page(pageNumber));
               pageFaults++;
            }
         } else {
            boolean pageExists = false;
            for (Page page : pageQueue) {
               if (page.pageNumber == pageNumber) {
                  pageExists = true;
                  break;
               }
            }
            if (!pageExists) {
               Page removedPage = pageQueue.remove();
               pageQueue.add(new Page(pageNumber));
               pageFaults++;
            }
         }

         System.out.print("Pages in Frame: ");
         for (Page page : pageQueue) {
            System.out.print(page.pageNumber + " ");
         }
         System.out.println();
      }

      System.out.println("Total Page Faults: " + pageFaults);
   }
}
