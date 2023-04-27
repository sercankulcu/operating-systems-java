package Page_Replacement;

import java.util.LinkedList;

/*
 * Here's an example Java code that implements the First-In-First-Out (FIFO) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the FIFO Page Replacement algorithm by keeping track of the frames in a 
 * linked list, checking if a page is already in the frames, and if not, removing the oldest 
 * page and adding the current page to the end of the list. The number of page faults is 
 * returned as the result.
 * */

public class FIFOPageReplacement {
	
	static int pageFaults(String reference, int numberOfPages, int numberOfFrames) {
		
		LinkedList<String> frames = new LinkedList<>();
		int pageFaults = 0;
		
		String[] tokens = reference.split(",");

		for (String t : tokens) {
			
			System.out.println("request to page " + t);
			
			if (frames.size() < numberOfFrames) {
				if (!frames.contains(t)) {
					frames.addLast(t);
					pageFaults++;
					System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
				}
			} else {
				if (!frames.contains(t)) {
					frames.removeFirst();
					frames.addLast(t);
					pageFaults++;
					System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
				}
			}
		}
		return pageFaults;
	}

	public static void main(String[] args) {

		int numberOfPages = 8;
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		int numberOfFrames = 4;

		int pageFaults = pageFaults(reference, numberOfPages, numberOfFrames);
		System.out.println("Number of page faults: " + pageFaults);
	}
}
