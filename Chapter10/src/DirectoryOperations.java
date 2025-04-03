import java.io.File;
import java.util.Date;

/*
 * In Java, you can use the File class to perform various operations on directories, such as 
 * creating, deleting, and listing the contents of a directory. This enhanced program 
 * demonstrates multiple directory operations including:
 * - Creating single and nested directories
 * - Listing directory contents with additional information
 * - Checking directory properties
 * - Renaming directories
 * - Cleaning up created directories
 * 
 * This program creates directories, displays detailed information about files and 
 * directories, and demonstrates proper cleanup operations.
 */

public class DirectoryOperations {

    public static void main(String[] args) {
        // Create a single new directory in the current directory
        File directory = new File("new_directory");
        boolean created = directory.mkdir();
        
        // Check and report if the directory creation was successful
        if (created) {
            System.out.println("Directory created: " + directory.getName());
        } else {
            System.out.println("Directory creation failed");
        }

        // Create nested directories using mkdirs()
        File nestedDirectory = new File("parent/child/grandchild");
        boolean nestedCreated = nestedDirectory.mkdirs();
        
        // Verify nested directory creation
        if (nestedCreated) {
            System.out.println("Nested directories created: " + nestedDirectory.getPath());
        } else {
            System.out.println("Nested directory creation failed");
        }

        // List the contents of the current directory with additional details
        File currentDirectory = new File(".");
        File[] fileList = currentDirectory.listFiles();  // Using listFiles() for more details
        
        System.out.println("\nDetailed contents of current directory:");
        System.out.println("----------------------------------------");
        
        // Iterate through directory contents and display detailed information
        if (fileList != null) {
            for (File file : fileList) {
                String type = file.isDirectory() ? "Directory" : "File";
                long lastModified = file.lastModified();
                String dateModified = new Date(lastModified).toString();
                long size = file.length();
                
                System.out.printf("%-10s %-20s %-30s Size: %d bytes%n", 
                    type, 
                    file.getName(), 
                    "Last Modified: " + dateModified, 
                    size);
            }
        }

        // Demonstrate directory properties
        System.out.println("\nDirectory Properties:");
        System.out.println("----------------------------------------");
        System.out.println("Absolute path: " + directory.getAbsolutePath());
        System.out.println("Parent directory: " + directory.getParent());
        System.out.println("Free space: " + currentDirectory.getFreeSpace() + " bytes");
        System.out.println("Total space: " + currentDirectory.getTotalSpace() + " bytes");

        // Rename the directory
        File renamedDirectory = new File("renamed_directory");
        boolean renamed = directory.renameTo(renamedDirectory);
        
        // Check if renaming was successful
        if (renamed) {
            System.out.println("\nDirectory renamed to: " + renamedDirectory.getName());
        } else {
            System.out.println("\nDirectory rename failed");
        }

        // Cleanup: Delete the renamed directory
        boolean deleted = renamedDirectory.delete();
        if (deleted) {
            System.out.println("Directory deleted: " + renamedDirectory.getName());
        } else {
            System.out.println("Directory delete failed");
        }

        // Cleanup: Delete nested directories (must delete from deepest level up)
        File childDir = new File("parent/child");
        File parentDir = new File("parent");
        
        // Delete nested directories in reverse order
        if (nestedDirectory.delete() && childDir.delete() && parentDir.delete()) {
            System.out.println("Nested directories cleaned up successfully");
        } else {
            System.out.println("Nested directory cleanup failed");
        }
    }
}