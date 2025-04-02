package page_replacement;

import java.util.HashMap;
import java.util.LinkedList;

/*
 * Here's an example Java code that implements the Working Set Page Replacement algorithm in an Operating System context:
 * 
 * The Working Set Page Replacement algorithm maintains a "working set" of pages that have been referenced within a 
 * specified time window (delta). It uses a linked list to track pages in memory and a hash map to store the last 
 * access time of each page. When a page fault occurs and memory is full, the algorithm removes the page with the 
 * oldest access time that falls outside the working set window, replacing it with the new page. The number of page 
 * faults is tracked and returned as the result.
 * 
 * Key features:
 * - Uses LinkedList to maintain pages in memory
 * - Uses HashMap to track last access times
 * - Implements a time window (delta) to define the working set
 * - Provides detailed frame state output including timestamps
 * */

public class WorkingSetPageReplacement {

    // Method to calculate page faults using Working Set algorithm
    static int pageFaults(String reference, int numberOfFrames, int delta) {
        // LinkedList to store pages in memory
        LinkedList<String> frames = new LinkedList<>();
        
        // HashMap to store last access time for each page
        HashMap<String, Integer> lastAccessTime = new HashMap<>();
        
        // Counter for page faults
        int pageFaults = 0;
        
        // Current time stamp, incremented with each page request
        int currentTime = 0;
        
        // Split reference string into individual page requests
        String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
        for (String t : tokens) {
            // Display current page request and time
            System.out.println("Time " + currentTime + ": Request to page " + t);
            
            // Check if page is not in frames (page fault case)
            if (!frames.contains(t)) {
                // Case 1: Frames have available space
                if (frames.size() < numberOfFrames) {
                    // Add new page to frames
                    frames.add(t);
                    // Record current time as last access
                    lastAccessTime.put(t, currentTime);
                    // Increment page fault counter
                    pageFaults++;
                    // Display updated frame list and page fault count
                    System.out.println("Page " + t + " added to frame list " + frames + 
                                     " (Last access: " + currentTime + "), page fault is " + pageFaults);
                } 
                // Case 2: Frames are full, replace page outside working set
                else {
                    // Find page to replace (oldest outside delta window)
                    String pageToReplace = null;
                    int oldestTime = currentTime;
                    for (String page : frames) {
                        int lastTime = lastAccessTime.get(page);
                        // Check if page is outside working set window
                        if (currentTime - lastTime >= delta) {
                            pageToReplace = page;
                            break; // Replace first page outside window
                        }
                        // Track oldest page if no page is outside window
                        if (lastTime < oldestTime) {
                            oldestTime = lastTime;
                            pageToReplace = page;
                        }
                    }
                    // Remove the selected page
                    frames.remove(pageToReplace);
                    lastAccessTime.remove(pageToReplace);
                    // Add new page
                    frames.add(t);
                    lastAccessTime.put(t, currentTime);
                    // Increment page fault counter
                    pageFaults++;
                    // Display replacement and updated frame list
                    System.out.println("Page " + pageToReplace + " replaced with " + t + 
                                     " in frame list " + frames + " (Last access: " + currentTime + 
                                     "), page fault is " + pageFaults);
                }
            } 
            // Page is already in frames (hit case)
            else {
                // Update last access time for the page
                lastAccessTime.put(t, currentTime);
                // Display frame list (no page fault)
                System.out.println("Page " + t + " found in frame list " + frames + 
                                 " (Last access updated to: " + currentTime + ")");
            }
            // Increment time for next iteration
            currentTime++;
        }
        // Return total number of page faults
        return pageFaults;
    }

    // Main method to run the Working Set simulation
    public static void main(String[] args) {
        // Reference string representing sequence of page requests
        String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
        
        // Number of available memory frames
        int numberOfFrames = 4;
        
        // Delta value defining the working set window (time units)
        int delta = 3;

        // Execute Working Set algorithm and get total page faults
        int pageFaults = pageFaults(reference, numberOfFrames, delta);
        
        // Display final result
        System.out.println("\nNumber of page faults: " + pageFaults);
    }
}