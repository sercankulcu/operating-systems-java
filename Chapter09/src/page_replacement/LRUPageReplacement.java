package page_replacement;

import java.util.LinkedHashMap;

/*
 * Here's an example Java code that implements the Least Recently Used (LRU) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the LRU algorithm by using a linked hash map to keep track of the frames, 
 * where the order of the keys is the order in which they were last accessed. When a page is 
 * requested and is not found in the frames, the page is added to the frames if there is still 
 * capacity, and if not, the first key (i.e., the least recently used page) is removed to make 
 * room for the new page. The number of page faults is tracked and returned as the result.
 * 
 * Key features:
 * - Uses LinkedHashMap with access-order to track LRU
 * - Maintains frame capacity limit
 * - Updates page access order on hits
 * - Provides detailed frame state output
 * */

public class LRUPageReplacement {

    // Method to calculate page faults using LRU algorithm
	static int pageFaults(String reference, int numberOfFrames) {

        // LinkedHashMap to store pages (keys) and access counters (values), with access-order enabled
		LinkedHashMap<String, Integer> frames = new LinkedHashMap<>(numberOfFrames, 1.0f, true);
		
        // Counter for page faults
		int pageFaults = 0;

        // Split reference string into individual page requests
		String[] tokens = reference.split(",");

        // Counter to track access order of pages
		int counter = 0;
		
        // Iterate through each page request in the reference string
		for (String t : tokens) {
            // Display current page request
			System.out.println("Request to page " + t);
			
            // Check if page is not in frames (page fault case)
			if (!frames.containsKey(t)) {
                // If frames are full, remove least recently used page
				if (frames.size() == numberOfFrames) {
                    // Remove the first entry (LRU) using iterator
					frames.remove(frames.keySet().iterator().next());
				}
                // Add new page with current counter value and increment counter
				frames.put(t, counter++);
                // Increment page fault counter since page wasn't present
				pageFaults++;
                // Display updated frame list and page fault count
				System.out.println("Page " + t + " is added to frame list " + frames + 
                                 " page fault is " + pageFaults);
			}
            // Page is already in frames (hit case)
			else {
                // Update access order by re-inserting with new counter value
				frames.put(t, counter++);
                // Display updated frame list (no page fault)
				System.out.println("Page " + t + " is updated to frame list " + frames + 
                                 " page fault is " + pageFaults);
			}
		}
        // Return total number of page faults
		return pageFaults;
	}

    // Main method to run the LRU simulation
	public static void main(String[] args) {
        // Reference string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 3;

        // Execute LRU algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}
}