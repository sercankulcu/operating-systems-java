package page_replacement;

import java.util.LinkedList;

/*
 * Here's an example Java code that implements the Working Set Clock Page Replacement algorithm in an Operating System context:
 * 
 * The Working Set Clock Page Replacement algorithm combines the Working Set and Clock (Second Chance) algorithms. It uses a 
 * circular list of pages with a pointer to track the next replacement candidate and maintains a working set defined by a 
 * time window (delta). Each page has a reference bit and a last access time. When a page fault occurs and memory is full, 
 * the algorithm scans the list: pages within the working set window get a second chance (reference bit cleared), while 
 * pages outside the window with no reference bit are replaced. The number of page faults is tracked and returned.
 * 
 * Key features:
 * - Uses LinkedList as a circular buffer
 * - Tracks last access time and reference bits for each page
 * - Implements a working set window (delta) with second-chance mechanism
 * - Provides detailed frame state output including timestamps and reference bits
 * */

public class WorkingSetClockPageReplacement {
	
	// Class to represent a page with number, reference bit, and last access time
	static class ClockPage {
	    String pageNumber;     // Page identifier
	    boolean referenceBit;  // Flag indicating recent use
	    int lastAccessTime;    // Last time the page was accessed

	    // Constructor to initialize page with number and access time
	    ClockPage(String pageNumber, int lastAccessTime) {
	        this.pageNumber = pageNumber;
	        this.referenceBit = true; // New pages start as referenced
	        this.lastAccessTime = lastAccessTime;
	    }

	    // Override toString for readable frame list output
	    public String toString() {
	        return pageNumber + " (R:" + referenceBit + ", T:" + lastAccessTime + ")";
	    }
	}

    // Method to calculate page faults using Working Set Clock algorithm
    static int pageFaults(String reference, int numberOfFrames, int delta) {
        // LinkedList to store pages in frames, simulating circular buffer
        LinkedList<ClockPage> frames = new LinkedList<>();
        
        // Counter for page faults
        int pageFaults = 0;
        
        // Pointer to track current position in circular scan
        int pointer = 0;
        
        // Current time stamp, incremented with each page request
        int currentTime = 0;
        
        // Split reference string into individual page requests
        String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
        for (String t : tokens) {
            // Display current page request and time
            System.out.println("Time " + currentTime + ": Request to page " + t);
            
            // Check if page is not in frames (page fault case)
            if (!containsPage(frames, t)) {
                // Case 1: Frames have available space
                if (frames.size() < numberOfFrames) {
                    // Create new page with reference bit true and current time
                    ClockPage page = new ClockPage(t, currentTime);
                    // Add page to frames
                    frames.add(page);
                    // Increment page fault counter
                    pageFaults++;
                    // Display updated frame list and page fault count
                    System.out.println("Page " + t + " added to frame list " + frames + 
                                     ", page fault is " + pageFaults);
                } 
                // Case 2: Frames are full, use working set clock algorithm
                else {
                    // Scan frames until a page is replaced
                	int pointerBackup = pointer;
                    while (true) {
                        ClockPage page = frames.get(pointer); // Get page at pointer
                        int timeSinceLastAccess = currentTime - page.lastAccessTime;
                        
                        // Check if page is outside working set window and not referenced
                        if (timeSinceLastAccess >= delta && !page.referenceBit) {
                            // Replace page outside window with no second chance
                            page = new ClockPage(t, currentTime);
                            frames.set(pointer, page);
                            pointer = (pointer + 1) % numberOfFrames; // Move pointer
                            pageFaults++;
                            System.out.println("Page " + t + " replaced page at " + pointer + 
                                             " in frame list " + frames + ", page fault is " + pageFaults);
                            break;
                        } 
                        // Page within window or referenced gets a second chance
                        else if (page.referenceBit) {
                            // Clear reference bit and update last access time
                            page.referenceBit = false;
                            page.lastAccessTime = currentTime;
                            pointer = (pointer + 1) % numberOfFrames; // Move pointer
                        } 
                        // Page outside window but referenced, or within window
                        else {
                            pointer = (pointer + 1) % numberOfFrames; // Move pointer
                            if(pointer == pointerBackup) {
                            	page = new ClockPage(t, currentTime);
                                frames.set(pointer, page);
                                pageFaults++;
                                System.out.println("Page " + t + " replaced page at " + pointer + 
                                                 " in frame list " + frames + ", page fault is " + pageFaults);
                                break;
                            }
                        }
                    }
                }
            } 
            // Page is already in frames (hit case)
            else {
                // Update existing page's reference bit and last access time
                ClockPage page = getPage(frames, t);
                page.referenceBit = true;
                page.lastAccessTime = currentTime;
                // Display frame list (no page fault)
                System.out.println("Page " + t + " found in frame list " + frames);
            }
            // Increment time for next iteration
            currentTime++;
        }
        // Return total number of page faults
        return pageFaults;
    }

    // Helper method to check if a page exists in frames
    private static boolean containsPage(LinkedList<ClockPage> frames, String pageNumber) {
        for (ClockPage page : frames) {
            if (page.pageNumber.equals(pageNumber)) {
                return true; // Page found
            }
        }
        return false; // Page not found
    }

    // Helper method to retrieve a page from frames
    private static ClockPage getPage(LinkedList<ClockPage> frames, String pageNumber) {
        for (ClockPage page : frames) {
            if (page.pageNumber.equals(pageNumber)) {
                return page; // Return matching page
            }
        }
        return null; // Should not occur due to prior containsPage check
    }

    // Main method to run the Working Set Clock simulation
    public static void main(String[] args) {
        // Reference string representing sequence of page requests
        String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
        
        // Number of available memory frames
        int numberOfFrames = 4;
        
        // Delta value defining the working set window (time units)
        int delta = 3;

        // Execute Working Set Clock algorithm and get total page faults
        int pageFaults = pageFaults(reference, numberOfFrames, delta);
        
        // Display final result
        System.out.println("\nNumber of page faults: " + pageFaults);
    }
}
