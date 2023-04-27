package Page_Replacement;

import java.util.ArrayList;

/*
 * Here's an example Java code that implements the Optimal Page Replacement algorithm in an Operating System context:
 * 
 * This code implements the Optimal Page Replacement algorithm by keeping track of the frames 
 * in an ArrayList, checking if a page is already in the frames, and if not, finding the page 
 * that will be used farthest in the future and replacing it with the current page. The number 
 * of page faults is returned as the result.
 * */

public class OptimalPageReplacement {
	
	static int pageFaults(String reference, int numberOfPages, int numberOfFrames) {
    	
        ArrayList<String> frames = new ArrayList<>(numberOfFrames);
        int pageFaults = 0;
        
        
        String[] tokens = reference.split(",");

        int counter = 0;
    		for (String t : tokens) {
    			System.out.println("request to page " + t);
    			
            if (frames.size() < numberOfFrames) {
                if (!frames.contains(t)) {
                    frames.add(t);
                    pageFaults++;
                    System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
                }
            } else {
                if (!frames.contains(t)) {
                    int j = 0;
                    int farthest = counter;
                    for (int k = 0; k < numberOfFrames; k++) {
                        int l;
                        for (l = counter; l < numberOfFrames; l++) {
                            if (frames.get(k).equals(t)) {
                                break;
                            }
                        }
                        if (l > farthest) {
                            farthest = l;
                            j = k;
                        }
                    }
                    frames.set(j, t);
                    pageFaults++;
                    System.out.println("page " + t + " is added to frame list " + frames + " page fault is " + pageFaults);
                }
            }
            counter++;
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

