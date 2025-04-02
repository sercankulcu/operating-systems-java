package page_replacement;

import java.util.LinkedList;

/*
 * Here's an example Java code that implements the Second Chance Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Second Chance Page Replacement algorithm by keeping track of the 
 * frames in a linked list, checking if a page is already in the frames, and if not, iteratively 
 * removing the oldest page until a page without a reference bit is found and adding the current 
 * page to the end of the list. If a page is found in the frames, its reference bit is set to 
 * true. The number of page faults is returned as the result.
 * 
 * Key features:
 * - Uses LinkedList to maintain FIFO order with reference bit checking
 * - Gives pages a "second chance" based on reference bit
 * - Tracks and reports page faults
 * - Provides detailed frame state output including reference bits
 * */

public class SecondChancePageReplacement {

    // Method to calculate page faults using Second Chance algorithm
	static int pageFaults(String reference, int numberOfFrames) {
        // LinkedList to store pages in frames, maintaining insertion order
		LinkedList<Page> frames = new LinkedList<>();
		
        // Counter for page faults
		int pageFaults = 0;
		
        // Split reference string into individual page requests
		String[] tokens = reference.split(",");

        // Iterate through each page request in the reference string
		for (String t : tokens) {
            // Display current page request
			System.out.println("Request to page " + t);
			
            // Check if page is not in frames (page fault case)
			if (!containsPage(frames, t)) {
                // Case 1: Frames have available space
				if (frames.size() < numberOfFrames) {
                    // Create new page with reference bit set to true
					Page page = new Page(t);
                    // Add page to end of frames list
					frames.addLast(page);
                    // Increment page fault counter
					pageFaults++;
                    // Display updated frame list and page fault count
					System.out.println("Page " + t + " is added to frame list " + frames + 
                                     " page fault is " + pageFaults);
				} 
                // Case 2: Frames are full, use second chance algorithm
				else {
                    // Scan frames until a page is replaced
					while (true) {
                        // Remove oldest page (front of list)
						Page page = frames.removeFirst();
						if (page.referenceBit) {
                            // Give page a second chance: clear reference bit and re-add
							page.referenceBit = false;
							frames.addLast(page);
						} else {
                            // Replace page with no second chance
							page = new Page(t); // New page with reference bit true
							frames.addLast(page); // Add to end of list
                            // Increment page fault counter
							pageFaults++;
                            // Display updated frame list and page fault count
							System.out.println("Page " + t + " is added to frame list " + frames + 
                                             " page fault is " + pageFaults);
							break; // Exit loop after replacement
						}
					}
				}
			} 
            // Page is already in frames (hit case)
			else {
                // Get the existing page and set its reference bit to true
				Page page = getPage(frames, t);
				page.referenceBit = true;
                // Display frame list (no page fault)
				System.out.println("Page " + t + " is found in frame list " + frames);
			}
		}
        // Return total number of page faults
		return pageFaults;
	}

    // Helper method to check if a page exists in frames
	private static boolean containsPage(LinkedList<Page> frames, String pageNumber) {
		for (Page page : frames) {
			if (page.pageNumber.equals(pageNumber)) {
				return true; // Page found
			}
		}
		return false; // Page not found
	}

    // Helper method to retrieve a page from frames
	private static Page getPage(LinkedList<Page> frames, String pageNumber) {
		for (Page page : frames) {
			if (page.pageNumber.equals(pageNumber)) {
				return page; // Return matching page
			}
		}
		return null; // Should not occur due to prior containsPage check
	}

    // Main method to run the Second Chance simulation
	public static void main(String[] args) {
        // Reference string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 4;

        // Execute Second Chance algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}
}

// Class to represent a page with a number and reference bit
class Page {
	String pageNumber;    // Page identifier
	boolean referenceBit; // Flag indicating recent use

    // Constructor to initialize page with number and set reference bit to true
	Page(String pageNumber) {
		this.pageNumber = pageNumber;
		referenceBit = true; // New pages start as referenced
	}
	
    // Override toString for readable frame list output including reference bit
	public String toString() {
		return pageNumber + " " + referenceBit;
	}
}