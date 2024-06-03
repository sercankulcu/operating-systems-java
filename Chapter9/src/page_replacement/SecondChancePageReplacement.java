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
 * */

public class SecondChancePageReplacement {

	static int pageFaults(String reference, int numberOfFrames) {

		LinkedList<Page> frames = new LinkedList<>();
		int pageFaults = 0;
		
		String[] tokens = reference.split(",");

		for (String t : tokens) {
			
			System.out.println("request to page " + t);
			
			if (!containsPage(frames, t)) {
				if (frames.size() < numberOfFrames) {
					Page page = new Page(t);
					frames.addLast(page);
					pageFaults++;
					System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
				} else {
					while (true) {
						Page page = frames.removeFirst();
						if (page.referenceBit) {
							page.referenceBit = false;
							frames.addLast(page);
						} else {
							page = new Page(t);
							frames.addLast(page);
							pageFaults++;
							System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
							break;
						}
					}
				}
			} else {
				Page page = getPage(frames, t);
				page.referenceBit = true;
				System.out.println("page " + t + " is found in frame list " + frames);
			}
		}
		return pageFaults;
	}

	private static boolean containsPage(LinkedList<Page> frames, String pageNumber) {
		for (Page page : frames) {
			if (page.pageNumber.equals(pageNumber)) {
				return true;
			}
		}
		return false;
	}

	private static Page getPage(LinkedList<Page> frames, String pageNumber) {
		for (Page page : frames) {
			if (page.pageNumber.equals(pageNumber)) {
				return page;
			}
		}
		return null;
	}

	public static void main(String[] args) {

		int numberOfPages = 8;
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		int numberOfFrames = 4;

		int pageFaults = pageFaults(reference, numberOfFrames);
		System.out.println("Number of page faults: " + pageFaults);
	}
}

class Page {
	String pageNumber;
	boolean referenceBit;

	Page(String pageNumber) {
		this.pageNumber = pageNumber;
		referenceBit = true;
	}
	
	public String toString() {
		return pageNumber + " " + referenceBit;
	}
}
