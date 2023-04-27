import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates the use of TLBs:
 * 
 * Translation Lookaside Buffers (TLBs) are high-speed memory structures that are used to store 
 * the most recently accessed virtual-to-physical address translations. They are used in operating 
 * systems to speed up the process of translating virtual addresses to physical addresses, which 
 * is required when a program accesses memory.
 * 
 * To translate a virtual address, the page number and offset are calculated from the virtual 
 * address. The TLB is then searched for a translation with the matching page number. If the 
 * translation is found in the TLB, it is used to calculate the physical address. If the 
 * translation is not found in the TLB, it is looked up in the page table and added to the TLB. 
 * If the TLB is full, the oldest translation is removed using the poll method.
 * 
 * */

public class TLBExample {
    private static final int TLB_SIZE = 4;
    private static final int PAGE_TABLE_SIZE = 8;

    public static void main(String[] args) {
        // Create a TLB and a page table
        Queue<Translation> TLB = new LinkedList<>();
        Translation[] pageTable = new Translation[PAGE_TABLE_SIZE];
        for (int i = 0; i < PAGE_TABLE_SIZE; i++) {
            pageTable[i] = new Translation(i, i * 100);
        }
        // Translate a virtual address
        int virtualAddress = 123;
        int pageNumber = virtualAddress / 100;
        int offset = virtualAddress % 100;
        System.out.println("Translating virtual address " + virtualAddress + " (page number: " + pageNumber + ", offset: " + offset + ")");
        // Check the TLB for the translation
        Translation translation = null;
        for (Translation t : TLB) {
            if (t.pageNumber == pageNumber) {
                translation = t;
                break;
            }
        }
        if (translation == null) {
            // The translation was not found in the TLB, so look it up in the page table
            translation = pageTable[pageNumber];
            // Add the translation to the TLB
            if (TLB.size() == TLB_SIZE) {
                TLB.poll();
            }
            TLB.add(translation);
        }
        // Calculate the physical address
        int physicalAddress = translation.frameNumber * 100 + offset;
        System.out.println("Physical address: " + physicalAddress);
    }

    private static class Translation {
        int pageNumber;
        int frameNumber;

        Translation(int pageNumber, int frameNumber) {
            this.pageNumber = pageNumber;
            this.frameNumber = frameNumber;
        }
    }
}
