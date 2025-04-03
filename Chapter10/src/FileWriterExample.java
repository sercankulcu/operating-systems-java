import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * This program demonstrates multiple ways to write text to a file in Java and then cleans up
 * by deleting the created files. It includes:
 * 1. FileWriter (basic approach)
 * 2. BufferedWriter (efficient for larger data)
 * 3. PrintWriter (convenient for formatted output)
 * 4. Files class (modern approach from Java NIO)
 * 5. File deletion at the end
 * 
 * The program creates files, writes to them, and then deletes them as part of cleanup.
 */

public class FileWriterExample {
	
    public static void main(String[] args) {
        // Sample text to write
        String content = "Hello, World!\nThis is a test file.";
        
        // Array to keep track of created files for cleanup
        String[] fileNames = {"output1.txt", "output2.txt", "output3.txt", "output4.txt"};

        // Method 1: Using FileWriter
        try {
            FileWriter fileWriter = new FileWriter(fileNames[0], false);
            fileWriter.write(content);
            fileWriter.close();
            System.out.println("Successfully wrote to " + fileNames[0] + " using FileWriter");
        } catch (IOException e) {
            System.err.println("Error writing with FileWriter: " + e.getMessage());
        }

        // Method 2: Using BufferedWriter
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(fileNames[1], false)
            );
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.write("Additional line");
            bufferedWriter.close();
            System.out.println("Successfully wrote to " + fileNames[1] + " using BufferedWriter");
        } catch (IOException e) {
            System.err.println("Error writing with BufferedWriter: " + e.getMessage());
        }

        // Method 3: Using PrintWriter
        try {
            PrintWriter printWriter = new PrintWriter(fileNames[2], "UTF-8");
            printWriter.println(content);
            printWriter.printf("Formatted number: %.2f%n", 3.14159);
            printWriter.close();
            System.out.println("Successfully wrote to " + fileNames[2] + " using PrintWriter");
        } catch (IOException e) {
            System.err.println("Error writing with PrintWriter: " + e.getMessage());
        }

        // Method 4: Using Files class (Java NIO)
        try {
            Files.write(Paths.get(fileNames[3]), 
                       content.getBytes(), 
                       java.nio.file.StandardOpenOption.CREATE,
                       java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Successfully wrote to " + fileNames[3] + " using Files");
        } catch (IOException e) {
            System.err.println("Error writing with Files: " + e.getMessage());
        }

        // Cleanup Section: Delete all created files
        System.out.println("\n=== Cleanup: Deleting Files ===");
        
        // Iterate through all created files and attempt to delete them
        for (String fileName : fileNames) {
            File file = new File(fileName);
            
            // Check if file exists before attempting deletion
            if (file.exists()) {
            	
            	System.out.println(fileName + " size: " + file.length() + " bytes");
                // Attempt to delete the file
                boolean deleted = file.delete();
                
                // Report the result of the deletion attempt
                if (deleted) {
                    System.out.println("Successfully deleted: " + fileName);
                } else {
                    System.out.println("Failed to delete: " + fileName);
                }
            } else {
                System.out.println("File not found for deletion: " + fileName);
            }
        }
    }
}