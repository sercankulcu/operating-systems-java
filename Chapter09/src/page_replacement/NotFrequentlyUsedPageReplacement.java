package page_replacement;

import java.util.ArrayList;

/*
 * Here's an example Java code that implements the Not Frequently Used (NFU) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Not Frequently Used (NFU) algorithm by using an ArrayList to keep 
 * track of the frames and an array to keep track of the number of page accesses for each page 
 * in the frames. When a page is requested and is not found in the frames, the page is added to 
 * the frames if there is still capacity, and if not, the page with the least number of accesses 
 * is replaced. The number of accesses for each page is incremented after each iteration, and 
 * the number of page faults is tracked and returned as the result.
 * 
 * Key features:
 * - Uses ArrayList to manage memory frames
 * - Tracks page access frequency with an array
 * - Replaces least frequently used page on fault
 * - Provides frame state output for visualization
 * */

public class NotFrequentlyUsedPageReplacement {

    // Method to calculate page faults using NFU algorithm
	static int pageFaults(String reference, int numberOfFrames) {

        // ArrayList to store pages in frames
		ArrayList<String> frames = new ArrayList<>(numberOfFrames);
		
        // Counter for page faults
		int pageFaults = 0;
		
        // Array to track access frequency of each page in frames
		int[] pageCounter = new int[numberOfFrames];

        // Split reference string into individual page requests
		String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
		for (String t : tokens) {
            // Display current page request
			System.out.println("Request to page " + t);
			
            // Check if page is not in frames (page fault case)
			if (!frames.contains(t)) {
                // Case 1: Frames have available space
				if (frames.size() < numberOfFrames) {
                    // Add new page to frames
					frames.add(t);
				} 
                // Case 2: Frames are full, need to replace least frequently used page
				else {
                    // Find index of page with minimum access count
					int minCounterIndex = 0;
					for (int j = 0; j < frames.size(); j++) {
						if (pageCounter[j] < pageCounter[minCounterIndex]) {
							minCounterIndex = j; // Update index if lower count found
						}
					}
                    // Replace the least frequently used page with the new page
					frames.set(minCounterIndex, t);
				}
                // Increment page fault counter since page wasn't present
				pageFaults++;
                // Display updated frame list and page fault count
				System.out.println("Page " + t + " is added to frame list " + frames + 
                                 " page fault is " + pageFaults);
			}
			
            // Update access counters for all pages in frames
			for (int j = 0; j < frames.size(); j++) {
				if (frames.get(j).equals(t)) {
                    // Increment counter for the accessed page
					pageCounter[j]++;
				} else {
                    // Reset counter for non-accessed pages (simplified NFU variant)
					pageCounter[j] = 0;
				}
			}
		}
        // Return total number of page faults
		return pageFaults;
	}

    // Main method to run the NFU simulation
	public static void main(String[] args) {
        // Reference.ConcurrentLinkedQueue string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 4;

        // Execute NFU algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}
}