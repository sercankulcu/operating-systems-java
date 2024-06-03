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
 * */

public class LRUPageReplacement {

	static int pageFaults(String reference, int numberOfFrames) {

		LinkedHashMap<String, Integer> frames = new LinkedHashMap<>(numberOfFrames, 1.0f, true);
		int pageFaults = 0;

		String[] tokens = reference.split(",");

		int counter = 0;
		for (String t : tokens) {
			System.out.println("request to page " + t);
			if (!frames.containsKey(t)) {
				if (frames.size() == numberOfFrames) {
					frames.remove(frames.keySet().iterator().next());
				}
				frames.put(t, counter++);
				pageFaults++;
				System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
			}
			else {
				frames.put(t, counter++);
			}
		}
		return pageFaults;
	}

	public static void main(String[] args) {

		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
		int numberOfFrames = 3;

		int pageFaults = pageFaults(reference, numberOfFrames);
		System.out.println("Number of page faults: " + pageFaults);
	}
}
