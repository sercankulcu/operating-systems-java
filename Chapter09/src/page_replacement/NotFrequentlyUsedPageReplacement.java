package page_replacement;

import java.util.ArrayList;

/*
 * Here's an example Java code that implements the Not Frequently Used (NFU) Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Not Frequently Used (NFU) algorithm by using an ArrayList to keep 
 * track of the frames and an array to keep track of the number of page accesses for each page 
 * in the frames. When a page is requested and is not found in the frames, the page is added to 
 * the frames if there is still capacity, and if not, the page with the least number of accesses 
 * is replaced. The number of accesses for each page is incremented after each iteration, and 
 * the number of page faults is tracked and returned as the result.
 * 
 * */

public class NotFrequentlyUsedPageReplacement {

	static int pageFaults(String reference, int numberOfFrames) {

		ArrayList<String> frames = new ArrayList<>(numberOfFrames);
		int pageFaults = 0;
		int[] pageCounter = new int[numberOfFrames];

		String[] tokens = reference.split(",");

		for (String t : tokens) {

			System.out.println("request to page " + t);
			if (!frames.contains(t)) {
				if (frames.size() < numberOfFrames) {
					frames.add(t);
				} else {
					int minCounterIndex = 0;
					for (int j = 0; j < frames.size(); j++) {
						if (pageCounter[j] < pageCounter[minCounterIndex]) {
							minCounterIndex = j;
						}
					}
					frames.set(minCounterIndex, t);
				}
				pageFaults++;
				System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
			}
			for (int j = 0; j < frames.size(); j++) {
				if (frames.get(j).equals(t)) {
					pageCounter[j]++;
				} else {
					pageCounter[j] = 0;
				}
			}
		}
		return pageFaults;
	}

	public static void main(String[] args) {

		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		int numberOfFrames = 4;

		int pageFaults = pageFaults(reference, numberOfFrames);
		System.out.println("Number of page faults: " + pageFaults);
	}
}
