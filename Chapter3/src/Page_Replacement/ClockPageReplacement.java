package Page_Replacement;

import java.util.LinkedList;
import java.util.Scanner;

public class ClockPageReplacement {
    static int pageFaults(int pages[], int n, int capacity) {
        LinkedList<ClockPage> frames = new LinkedList<>();
        int pageFaults = 0;
        int pointer = 0;
        for (int i = 0; i < n; i++) {
            if (frames.size() < capacity) {
                if (!containsPage(frames, pages[i])) {
                	ClockPage page = new ClockPage(pages[i]);
                    frames.add(page);
                    pageFaults++;
                } else {
                	ClockPage page = getPage(frames, pages[i]);
                    page.referenceBit = true;
                }
            } else {
                while (true) {
                	ClockPage page = frames.get(pointer);
                    if (page.referenceBit) {
                        page.referenceBit = false;
                        pointer = (pointer + 1) % capacity;
                    } else {
                        page = new ClockPage(pages[i]);
                        frames.set(pointer, page);
                        pointer = (pointer + 1) % capacity;
                        pageFaults++;
                        break;
                    }
                }
            }
        }
        return pageFaults;
    }

    private static boolean containsPage(LinkedList<ClockPage> frames, int pageNumber) {
        for (ClockPage page : frames) {
            if (page.pageNumber == pageNumber) {
                return true;
            }
        }
        return false;
    }

    private static ClockPage getPage(LinkedList<ClockPage> frames, int pageNumber) {
        for (ClockPage page : frames) {
            if (page.pageNumber == pageNumber) {
                return page;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of pages:");
        int n = sc.nextInt();
        int pages[] = new int[n];
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }
        System.out.println("Enter number of frames:");
        int capacity = sc.nextInt();
        int pageFaults = pageFaults(pages, n, capacity);
        System.out.println("Number of page faults: " + pageFaults);
    }
}

class ClockPage {
    int pageNumber;
    boolean referenceBit;

    ClockPage(int pageNumber) {
        this.pageNumber = pageNumber;
        referenceBit = true;
    }
}
