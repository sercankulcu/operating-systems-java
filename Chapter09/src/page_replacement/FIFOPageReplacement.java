package page_replacement;

import java.util.LinkedList;

/*
 * Here's an example Java code that implements the First-In-First-Out (FIFO) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the FIFO Page Replacement algorithm by keeping track of the frames in a 
 * linked list, checking if a page is already in the frames, and if not, removing the oldest 
 * page and adding the current page to the end of the list. The number of page faults is 
 * returned as the result.
 * 
 * Key features:
 * - Uses LinkedList to maintain FIFO order of pages
 * - Simulates memory frames with a fixed capacity
 * - Tracks and reports page faults
 * - Provides detailed output of frame state changes
 * */

public class FIFOPageReplacement {
	
    // Method to calculate page faults using FIFO algorithm
	static int pageFaults(String reference, int numberOfFrames) {
		
        // LinkedList to store pages in frames, maintaining FIFO order
		LinkedList<String> frames = new LinkedList<>();
		
        // Counter for page faults
		int pageFaults = 0;
		
        // Split reference string into individual page requests
		String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
		for (String t : tokens) {
			
            // Display current page request
			System.out.println("Request to page " + t);
			
            // Case 1: Frames have available space
			if (frames.size() < numberOfFrames) {
                // Check if page is already in frames
				if (!frames.contains(t)) {
                    // Add new page to end of frames list
					frames.addLast(t);
                    // Increment page fault counter since page wasn't present
					pageFaults++;
                    // Display updated frame list and page fault count
					System.out.println("Page " + t + " is added to frame list " + frames + 
                                     " page fault is " + pageFaults);
				} // No action if page is already present (no page fault)
			} 
            // Case 2: Frames are full
			else {
                // Check if page is already in frames
				if (!frames.contains(t)) {
                    // Remove oldest page (first in list) - FIFO policy
					frames.removeFirst();
                    // Add new page to end of frames list
					frames.addLast(t);
                    // Increment page fault counter since page wasn't present
					pageFaults++;
                    // Display updated frame list and page fault count
					System.out.println("Page " + t + " is added to frame list " + frames + 
                                     " page fault is " + pageFaults);
				} // No action if page is already present (no page fault)
			}
		}
        // Return total number of page faults
		return pageFaults;
	}

    // Main method to run the FIFO simulation
	public static void main(String[] args) {
        // Reference string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 3;

        // Execute FIFO algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}
}