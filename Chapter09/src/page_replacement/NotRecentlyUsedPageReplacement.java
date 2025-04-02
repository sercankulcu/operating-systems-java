package page_replacement;
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
 * Key features:
 * - Uses a Queue to maintain page order (FIFO-like base)
 * - Tracks reference status for each page
 * - Prioritizes replacement of "not referenced" pages
 * - Provides frame state output for visualization
 * */

public class NotRecentlyUsedPageReplacement {
	
    // Method to calculate page faults using NRU algorithm
	static int pageFaults(String reference, int numberOfFrames) {
        // Queue to store pages in memory, maintaining insertion order
		Queue<Page> frames = new LinkedList<>();
		
        // Counter for page faults
		int pageFaults = 0;
		
        // Split reference string into individual page requests
		String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
		for (String t : tokens) {
            // Display current page request
			System.out.println("Request to page " + t);
			
            // Check if the page is already in memory
			boolean found = false;
			for (Page p : frames) {
				if (p.number.equals(t)) {
                    // Page found: mark as referenced and avoid fault
					found = true;
					p.referenced = true;
					break;
				}
			}
			
            // Handle page not found in memory (page fault case)
			if (!found) {
                // Case 1: Frames are full, need to replace a page
				if (frames.size() == numberOfFrames) {
                    // Find the oldest "not referenced" page to replace
					Page toReplace = null;
					for (Page p : frames) {
						if (!p.referenced) {
							toReplace = p;
							break; // Stop at first "not referenced" page
						}
					}
                    // If no "not referenced" page found, use oldest page
					if (toReplace == null) {
						toReplace = frames.poll(); // Remove oldest page (front of queue)
					}
                    // Remove the selected page from frames
					frames.remove(toReplace);
				}
                // Add new page to frames, initially not referenced
				frames.add(new Page(t, false));
                // Increment page fault counter
				pageFaults++;
                // Display updated frame list
				System.out.println("Page " + t + " is added to frame list " + frames);
			} else {
                // Page was found, no fault occurred
				System.out.println("Page " + t + " is found");
			}
		}
        // Return total number of page faults
		return pageFaults;
	}

    // Main method to run the NRU simulation
	public static void main(String[] args) {
        // Reference string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 3;

        // Execute NRU algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}

    // Inner class to represent a page with number and reference status
	private static class Page {
		String number;      // Page identifier
		boolean referenced; // Flag indicating recent use

        // Constructor to initialize page with number and reference status
		Page(String number, boolean referenced) {
			this.number = number;
			this.referenced = referenced;
		}
		
        // Override toString for readable frame list output
		public String toString() {
			return number;
		}
	}
}