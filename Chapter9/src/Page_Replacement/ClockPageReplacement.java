package Page_Replacement;

import java.util.LinkedList;

/*
 * Here is an example of a Java program that implements the Clock page replacement algorithm:
 * 
 * */

public class ClockPageReplacement {

	static int pageFaults(String reference, int numberOfPages, int numberOfFrames) {

		LinkedList<ClockPage> frames = new LinkedList<>();
		int pageFaults = 0;
		int pointer = 0;

		String[] tokens = reference.split(",");

		for (String t : tokens) {
			
			System.out.println("request to page " + t);
			
			if (!containsPage(frames, t)) {
				if (frames.size() < numberOfFrames) {
					ClockPage page = new ClockPage(t);
					frames.add(page);
					pageFaults++;
					System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
				} else {
					while (true) {
						ClockPage page = frames.get(pointer);
						if (page.referenceBit) {
							page.referenceBit = false;
							pointer = (pointer + 1) % numberOfFrames;
						} else {
							page = new ClockPage(t);
							frames.set(pointer, page);
							pointer = (pointer + 1) % numberOfFrames;
							pageFaults++;
							System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
							break;
						}
					}
				}
			}
			else {
				ClockPage page = getPage(frames, t);
				page.referenceBit = true;
				System.out.println("page " + t + " is found in frame list " + frames);
			}
		}
		return pageFaults;
	}

	private static boolean containsPage(LinkedList<ClockPage> frames, String pageNumber) {
		for (ClockPage page : frames) {
			if (page.pageNumber.equals(pageNumber)) {
				return true;
			}
		}
		return false;
	}

	private static ClockPage getPage(LinkedList<ClockPage> frames, String pageNumber) {
		for (ClockPage page : frames) {
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

		int pageFaults = pageFaults(reference, numberOfPages, numberOfFrames);
		System.out.println("Number of page faults: " + pageFaults);
	}
}

class ClockPage {
	String pageNumber;
	boolean referenceBit;

	ClockPage(String pageNumber) {
		this.pageNumber = pageNumber;
		referenceBit = true;
	}

	public String toString() {
		return pageNumber + " " + referenceBit;
	}
}
