package page_replacement;

import java.util.*;

public class OptimalPageReplacement {
	
	public static void main(String[] args) {
		
		// Define the reference string and number of frames
		int[] referenceString = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1};
    int frames = 3;

		// Call the optimal page replacement algorithm
		int pageFaults = optimalPageReplacement(referenceString, frames);

		// Print the result
		System.out.println("Total Page Faults: " + pageFaults);
	}

	public static int optimalPageReplacement(int[] referenceString, int frames) {
		List<Integer> memory = new ArrayList<>(frames);
		int pageFaults = 0;

		for (int i = 0; i < referenceString.length; i++) {
			int currentPage = referenceString[i];

			// If the current page is not in memory, a page fault occurs
			if (!memory.contains(currentPage)) {
				pageFaults++;

				// If there is space in memory, add the page
				if (memory.size() < frames) {
					memory.add(currentPage);
				} else {
					// Find the page to replace using the optimal strategy
					int pageToReplace = findOptimalPageToReplace(memory, referenceString, i);
					memory.set(pageToReplace, currentPage);
				}
			}

			// Print current state of memory for visualization
			System.out.println("Memory after accessing page " + currentPage + ": " + memory);
		}

		return pageFaults;
	}

	private static int findOptimalPageToReplace(List<Integer> memory, int[] referenceString, int currentIndex) {
		int farthestIndex = currentIndex;
		int pageToReplace = -1;

		for (int i = 0; i < memory.size(); i++) {
			int nextUseIndex = findNextUseIndex(referenceString, currentIndex, memory.get(i));
			if (nextUseIndex == -1) {
				return i; // If a page is not going to be used again, return its index
			}
			if (nextUseIndex > farthestIndex) {
				farthestIndex = nextUseIndex;
				pageToReplace = i;
			}
		}

		return pageToReplace != -1 ? pageToReplace : 0;
	}

	private static int findNextUseIndex(int[] referenceString, int currentIndex, int page) {
		for (int i = currentIndex + 1; i < referenceString.length; i++) {
			if (referenceString[i] == page) {
				return i;
			}
		}
		return -1; // Page will not be used again
	}
}

