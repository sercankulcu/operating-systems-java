package page_replacement;

import java.util.LinkedList;

/*
 * Here is an example of a Java program that implements the Clock page replacement algorithm:
 * 
 * The Clock (or Second-Chance) Page Replacement algorithm uses a circular list of pages and a 
 * pointer to track the next candidate for replacement. Each page has a reference bit that is 
 * set to true when accessed. When a page fault occurs and memory is full, the algorithm scans 
 * the list starting at the pointer: if a page's reference bit is true, it gets a "second chance" 
 * (bit set to false), and the pointer moves forward; if false, the page is replaced. This 
 * continues until a replacement occurs.
 * 
 * Key features:
 * - Uses LinkedList as a circular buffer
 * - Implements reference bits for second-chance mechanism
 * - Tracks page faults and frame state
 * - Provides detailed output for visualization
 * */

public class ClockPageReplacement {
	
	// Class to represent a page with a number and reference bit
	static class ClockPage {
		String pageNumber;    // Page identifier
		boolean referenceBit; // Flag indicating recent use

	    // Constructor to initialize page with number and set reference bit to true
		ClockPage(String pageNumber) {
			this.pageNumber = pageNumber;
			referenceBit = true; // New pages start as referenced
		}

	    // Override toString for readable frame list output including reference bit
		public String toString() {
			return pageNumber + " " + referenceBit;
		}
	}

    // Method to calculate page faults using Clock algorithm
	static int pageFaults(String reference, int numberOfFrames) {
        // LinkedList to store pages in frames, simulating circular buffer
		LinkedList<ClockPage> frames = new LinkedList<>();
		
        // Counter for page faults
		int pageFaults = 0;
		
        // Pointer to track current position in circular scan
		int pointer = 0;

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
					ClockPage page = new ClockPage(t);
                    // Add page to frames
					frames.add(page);
                    // Increment page fault counter
					pageFaults++;
                    // Display updated frame list and page fault count
					System.out.println("Page " + t + " is added to frame list " + frames + 
                                     " page fault is " + pageFaults);
				} 
                // Case 2: Frames are full, use clock algorithm to replace
				else {
                    // Scan frames until a page is replaced
					while (true) {
						ClockPage page = frames.get(pointer); // Get page at pointer
						if (page.referenceBit) {
                            // Give page a second chance: clear reference bit
							page.referenceBit = false;
                            // Move pointer to next position (circular)
							pointer = (pointer + 1) % numberOfFrames;
						} else {
                            // Replace page with no second chance
							page = new ClockPage(t); // New page with reference bit true
							frames.set(pointer, page); // Replace at pointer position
                            // Move pointer to next position (circular)
							pointer = (pointer + 1) % numberOfFrames;
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
				ClockPage page = getPage(frames, t);
				page.referenceBit = true;
                // Display frame list (no page fault)
				System.out.println("Page " + t + " is found in frame list " + frames);
			}
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

    // Main method to run the Clock simulation
	public static void main(String[] args) {
        // Reference string representing sequence of page requests
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		
        // Number of available memory frames
		int numberOfFrames = 4;

        // Execute Clock algorithm and get total page faults
		int pageFaults = pageFaults(reference, numberOfFrames);
		
        // Display final result
		System.out.println("Number of page faults: " + pageFaults);
	}
}
