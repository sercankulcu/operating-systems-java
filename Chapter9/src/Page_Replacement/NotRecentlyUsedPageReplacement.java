package Page_Replacement;
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
 * */

public class NotRecentlyUsedPageReplacement {
	
	static int pageFaults(String reference, int numberOfPages, int numberOfFrames) {
		// Create a queue to store the pages in memory
		Queue<Page> frames = new LinkedList<>();
		int pageFaults = 0;
		String[] tokens = reference.split(",");

		for (String t : tokens) {
			
			System.out.println("request to page " + t);
			// Check if the page is already in memory
			boolean found = false;
			for (Page p : frames) {
				if (p.number.equals(t)) {
					found = true;
					p.referenced = true;
					break;
				}
			}
			if (!found) {
				// The page is not in memory, so we need to bring it in
				if (frames.size() == numberOfFrames) {
					// The page table is full, so we need to replace a page
					// Find the oldest "not referenced" page to replace
					Page toReplace = null;
					for (Page p : frames) {
						if (!p.referenced) {
							toReplace = p;
							break;
						}
					}
					if (toReplace == null) {
						// All pages are "referenced", so we replace the oldest page
						toReplace = frames.poll();
					}
					frames.remove(toReplace);
				}
				// Add the new page to the page table
				frames.add(new Page(t, true));
				pageFaults++;
				System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
			} else {
				System.out.println("page " + t + " is found in frame list " + frames);
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

	private static class Page {
		String number;
		boolean referenced;

		Page(String number, boolean referenced) {
			this.number = number;
			this.referenced = referenced;
		}
		
		public String toString() {
			return number;
		}
	}
}
