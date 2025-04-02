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
 * This extended version includes:
 * - Multiple address translations to show TLB behavior over time
 * - Hit/miss tracking to demonstrate TLB efficiency
 * - TLB contents display after each translation
 * - Error handling for invalid page numbers
 * - Configurable page size
 * 
 * The program simulates memory address translation by:
 * 1. Calculating page number and offset from virtual addresses
 * 2. Checking TLB for existing translations (hit)
 * 3. Falling back to page table on miss
 * 4. Managing TLB entries with FIFO replacement
 * */

public class TLBExample {
    // Constants for TLB and memory configuration
    private static final int TLB_SIZE = 4;          // Maximum number of TLB entries
    private static final int PAGE_TABLE_SIZE = 8;   // Number of pages in page table
    private static final int PAGE_SIZE = 100;       // Size of each page in bytes

    // Class to represent a virtual-to-physical address translation
    private static class Translation {
        int pageNumber;    // Virtual page number
        int frameNumber;   // Physical frame number

        // Constructor to initialize translation entry
        Translation(int pageNumber, int frameNumber) {
            this.pageNumber = pageNumber;
            this.frameNumber = frameNumber;
        }

        // String representation for debugging/display
        @Override
        public String toString() {
            return "Page " + pageNumber + " -> Frame " + frameNumber;
        }
    }

    public static void main(String[] args) {
        // Create TLB and page table structures
        Queue<Translation> TLB = new LinkedList<>();         // TLB with FIFO replacement
        Translation[] pageTable = new Translation[PAGE_TABLE_SIZE]; // Page table array
        
        // Initialize page table with sample mappings (page number to frame number)
        for (int i = 0; i < PAGE_TABLE_SIZE; i++) {
            pageTable[i] = new Translation(i, i * 100); // Simple mapping: frame = page * 100
        }

        // Array of virtual addresses to simulate memory requests
        int[] virtualAddresses = {123, 245, 123, 567, 789, 234, 345, 123};
        
        // Track TLB performance
        int tlbHits = 0;
        int tlbMisses = 0;

        System.out.println("=== TLB Simulation Start ===");
        
        // Process each virtual address
        for (int virtualAddress : virtualAddresses) {
            // Calculate page number and offset from virtual address
            int pageNumber = virtualAddress / PAGE_SIZE;
            int offset = virtualAddress % PAGE_SIZE;
            
            System.out.println("\nTranslating virtual address " + virtualAddress + 
                             " (page: " + pageNumber + ", offset: " + offset + ")");

            // Check if page number is valid
            if (pageNumber >= PAGE_TABLE_SIZE) {
                System.out.println("Error: Page number " + pageNumber + " exceeds page table size");
                continue;
            }

            // Search TLB for existing translation
            Translation translation = null;
            for (Translation t : TLB) {
                if (t.pageNumber == pageNumber) {
                    translation = t;
                    tlbHits++; // Increment hit counter
                    System.out.println("TLB Hit!");
                    break;
                }
            }

            // Handle TLB miss
            if (translation == null) {
                tlbMisses++; // Increment miss counter
                System.out.println("TLB Miss - Looking up in page table");
                // Fetch translation from page table
                translation = pageTable[pageNumber];
                
                // Manage TLB: remove oldest entry if full, then add new translation
                if (TLB.size() == TLB_SIZE) {
                    Translation removed = TLB.poll(); // Remove oldest entry (FIFO)
                    System.out.println("TLB full - Removed: " + removed);
                }
                TLB.add(translation); // Add new translation to TLB
                System.out.println("Added to TLB: " + translation);
            }

            // Calculate and display physical address
            int physicalAddress = translation.frameNumber + offset; // Frame number + offset
            System.out.println("Physical address: " + physicalAddress);

            // Display current TLB contents
            System.out.println("Current TLB contents:");
            for (Translation t : TLB) {
                System.out.println("  " + t);
            }
        }

        // Display final statistics
        System.out.println("\n=== TLB Simulation Summary ===");
        System.out.println("Total TLB Hits: " + tlbHits);
        System.out.println("Total TLB Misses: " + tlbMisses);
        System.out.println("Hit Rate: " + 
            (tlbHits * 100.0 / (tlbHits + tlbMisses)) + "%");
    }
}