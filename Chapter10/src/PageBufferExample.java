import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
 * This enhanced Java program demonstrates reading a file using a page buffer approach with:
 * - Configurable page size
 * - Line counting and statistics
 * - Error handling and reporting
 * - Page processing with multiple options
 * - Performance tracking
 * 
 * The program uses BufferedReader to read data page-by-page, processes the content,
 * and provides statistics about the reading operation. This approach is particularly
 * efficient for large files as it avoids loading the entire file into memory.
 */

public class PageBufferExample {
    // Constants for configuration
    private static final int PAGE_SIZE = 1000;  // Number of characters per page
    private static final String FILE_PATH = "data.txt";  // Default input file
    
    // Class variables to track statistics
    private static int totalPages = 0;
    private static int totalLines = 0;
    private static long totalChars = 0;

    public static void main(String[] args) {
        // Record start time for performance measurement
        long startTime = System.currentTimeMillis();
        
        // List to store processed pages (for demonstration)
        List<String> processedPages = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_PATH, StandardCharsets.UTF_8))) {
            // Buffer to hold page data
            char[] buffer = new char[PAGE_SIZE];
            int numChars;
            
            // Read file page by page until end is reached (-1)
            while ((numChars = reader.read(buffer)) != -1) {
                totalPages++;
                totalChars += numChars;
                
                // Create string from buffer, using exact number of characters read
                String page = new String(buffer, 0, numChars);
                
                // Process the page and store result
                String processedPage = processPage(page);
                processedPages.add(processedPage);
            }
            
            // Display reading statistics
            printStatistics(startTime);
            
        } catch (IOException e) {
            // Detailed error reporting
            System.err.println("Error reading file: " + FILE_PATH);
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Optional: Display summary of processed content
        System.out.println("\nProcessed Pages Summary:");
        System.out.println("------------------------");
        for (int i = 0; i < processedPages.size(); i++) {
            System.out.printf("Page %d: %d characters%n", i + 1, processedPages.get(i).length());
        }
    }

    /*
     * Processes a page of text and returns the processed result
     * @param page The string content of the current page
     * @return Processed string
     */
    private static String processPage(String page) {
        // Count lines in this page
        int linesInPage = countLines(page);
        totalLines += linesInPage;
        
        // Example processing: convert to uppercase and trim
        String processed = page.toUpperCase().trim();
        
        // Print page info (for demonstration)
        System.out.printf("Processing page %d: %d chars, %d lines%n", 
                         totalPages, page.length(), linesInPage);
        
        return processed;
    }
    
    /*
     * Counts the number of lines in a page of text
     * @param page The text to analyze
     * @return Number of lines found
     */
    private static int countLines(String page) {
        return (int)page.lines().count();
    }
    
    /*
     * Prints statistics about the file reading operation
     * @param startTime The time when reading began
     */
    private static void printStatistics(long startTime) {
        long endTime = System.currentTimeMillis();
        double seconds = (endTime - startTime) / 1000.0;
        
        System.out.println("\nReading Statistics:");
        System.out.println("-------------------");
        System.out.printf("Total pages read: %d%n", totalPages);
        System.out.printf("Total characters: %d%n", totalChars);
        System.out.printf("Total lines: %d%n", totalLines);
        System.out.printf("Processing time: %.3f seconds%n", seconds);
        System.out.printf("Average chars per page: %.1f%n", 
                         totalPages > 0 ? (double)totalChars / totalPages : 0);
    }
}