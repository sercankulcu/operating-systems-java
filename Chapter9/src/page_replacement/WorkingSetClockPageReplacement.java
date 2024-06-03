package page_replacement;

import java.util.*;

public class WorkingSetClockPageReplacement {

	private static int clockHand = 0;

	private static final int REFERENCE_BIT = 1;
	
	static int pageFaults(String reference, int numberOfFrames, int workingSetSize) {

		int pageFaults = 0;
		List<String> buffer = new ArrayList<>(Collections.nCopies(numberOfFrames, null));
		List<Integer> referenceBits = new ArrayList<>(Collections.nCopies(numberOfFrames, 0));
		List<String> workingSet = new ArrayList<>();

		String[] tokens = reference.split(",");

		for (String t : tokens) {
			System.out.println("request to page " + t);

			if (!workingSet.contains(t)) {
				System.out.println("page " + t + " not found in working set " + workingSet);
				if (workingSet.size() == workingSetSize) {
					int indexToRemove = findPageToRemove(numberOfFrames, buffer, referenceBits, workingSet);
					buffer.set(indexToRemove, t);
					referenceBits.set(indexToRemove, REFERENCE_BIT);
					workingSet.add(t);
				} else {
					int firstFreeIndex = buffer.indexOf(null);
					buffer.set(firstFreeIndex, t);
					referenceBits.set(firstFreeIndex, REFERENCE_BIT);
					workingSet.add(t);
				}
				pageFaults++;
			} else {
				int index = buffer.indexOf(t);
				referenceBits.set(index, REFERENCE_BIT);
				System.out.println("page " + t + " found in working set " + workingSet);
			}

			clockHand = (clockHand + 1) % numberOfFrames;
		}
		return pageFaults;
	}

	private static int findPageToRemove(int numberOfFrames, List<String> buffer, List<Integer> referenceBits, List<String> workingSet) {
		while (true) {
			int index = clockHand;
			int count = 0;

			do {
				String page = buffer.get(index);

				if (workingSet.contains(page) && referenceBits.get(index) == 0) {
					workingSet.remove(page);
					return index;
				}

				if (referenceBits.get(index) == 0) {
					workingSet.remove(page);
					return index;
				}

				referenceBits.set(index, 0);
				index = (index + 1) % numberOfFrames;
				count++;
			} while (count < numberOfFrames);
		}
	}

	public static void main(String[] args)
	{
		String reference = "7,0,1,2,0,3,0,4,2,3,0,3,0,3,2,1,2,0,1,7,0,1";
		int numberOfFrames = 4;
		int workingSetSize = 4;

		int pageFaults = pageFaults(reference, numberOfFrames, workingSetSize);
		System.out.println("Number of page faults: " + pageFaults);
	}
}
