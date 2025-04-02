package page_replacement;

import java.util.*;

/*
 * Here's an example Java code that implements the Optimal Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Optimal Page Replacement algorithm, which replaces the page that will not be used 
 * for the longest period of time in the future. It uses a list to represent memory frames and tracks page faults 
 * by simulating page requests from a reference string. The algorithm looks ahead in the reference string to 
 * determine which page to replace when memory is full.
 * 
 * Key features:
 * - Uses ArrayList to manage memory frames
 * - Predicts future page usage for optimal replacement
 * - Tracks and reports page faults
 * - Displays memory state after each page access
 * */

public class OptimalPageReplacement {

    // Main method to run the optimal page replacement simulation
	public static void main(String[] args) {
        // Define the reference string (sequence of page requests) and number of frames
		int[] referenceString = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
		int frames = 3;

        // Call the optimal page replacement algorithm and store result
		int pageFaults = optimalPageReplacement(referenceString, frames);

        // Print the total number of page faults
		System.out.println("Total Page Faults: " + pageFaults);
	}

    // Method to calculate page faults using the optimal algorithm
	public static int optimalPageReplacement(int[] referenceString, int frames) {
        // List to represent memory frames, initialized with capacity
		List<Integer> memory = new ArrayList<>(frames);
		
        // Counter for page faults
		int pageFaults = 0;

        // Iterate through each page request in the reference string
		for (int i = 0; i < referenceString.length; i++) {
			int currentPage = referenceString[i]; // Current page being requested

            // Check if the current page is already in memory
			if (!memory.contains(currentPage)) {
                // Increment page fault counter since page isn't present
				pageFaults++;

                // Case 1: Memory has space available
				if (memory.size() < frames) {
                    // Add the new page to memory
					memory.add(currentPage);
				} 
                // Case 2: Memory is full, need to replace a page
				else {
                    // Find the optimal page to replace based on future use
					int pageToReplace = findOptimalPageToReplace(memory, referenceString, i);
                    // Replace the selected page with the current page
					memory.set(pageToReplace, currentPage);
				}
			}

            // Display current memory state after processing the page
			System.out.println("Memory after accessing page " + currentPage + ": " + memory);
		}

        // Return total number of page faults
		return pageFaults;
	}

    // Helper method to find the optimal page to replace
	private static int findOptimalPageToReplace(List<Integer> memory, int[] referenceString, int currentIndex) {
        // Track the farthest future use and the page to replace
		int farthestIndex = currentIndex;
		int pageToReplace = -1;

        // Check each page in memory
		for (int i = 0; i < memory.size(); i++) {
            // Find the next use of the current page in the reference string
			int nextUseIndex = findNextUseIndex(referenceString, currentIndex, memory.get(i));
			
            // If page won't be used again, it's the optimal choice to replace
			if (nextUseIndex == -1) {
				return i; // Return index immediately
			}
            // Update if this page's next use is farther than the current farthest
			if (nextUseIndex > farthestIndex) {
				farthestIndex = nextUseIndex;
				pageToReplace = i;
			}
		}

        // Return the page to replace (default to 0 if no better option found)
		return pageToReplace != -1 ? pageToReplace : 0;
	}

    // Helper method to find the next occurrence of a page in the reference string
	private static int findNextUseIndex(int[] referenceString, int currentIndex, int page) {
        // Look ahead in the reference string starting from the next index
		for (int i = currentIndex + 1; i < referenceString.length; i++) {
			if (referenceString[i] == page) {
				return i; // Return index of next use
			}
		}
        // Return -1 if page is not used again
		return -1;
	}
}