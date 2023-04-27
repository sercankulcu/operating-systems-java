package Page_Replacement;
import java.util.LinkedList;

/*
 * Here is an example of a Java program that implements the Working Set page replacement algorithm:
 * 
 * */

import java.util.Queue;

public class WorkingSetPageReplacement {

	static int pageFaults(String reference, int numberOfPages, int numberOfFrames) {
		// Create a queue to store the pages in memory
		Queue<Page> frames = new LinkedList<>();
		int pageFaults = 0;
		
		// Process the page requests
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
					// Find the page with the lowest "age"
					Page toReplace = null;
					int minAge = Integer.MAX_VALUE;
					for (Page p : frames) {
						int age = p.age + (p.referenced ? 1 : 0);
						if (age < minAge) {
							toReplace = p;
							minAge = age;
						}
					}
					frames.remove(toReplace);
				}
				// Add the new page to the page table
				frames.add(new Page(t, true));
				pageFaults++;
				System.out.println("page " + t + " not found in frame list " + frames);
			} else {
				System.out.println("page " + t + " found in frame list " + frames);
			}
			// Increment the "age" of each page in the page table
			for (Page p : frames) {
				p.age++;
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
		int age;

		Page(String number, boolean referenced) {
			this.number = number;
			this.referenced = referenced;
			this.age = 0;
		}
		
		public String toString() {
			return number + "(" + age + ")";
		}
	}
}

