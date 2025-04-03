import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * In Java, the File class provides methods to retrieve various attributes and perform operations 
 * on files and directories. This enhanced program demonstrates:
 * - Basic file attributes (name, size, modification time)
 * - File permissions and status checks
 * - Path-related information
 * - File existence and type verification
 * - Additional file system information
 * 
 * The program examines a file "test.txt" and provides comprehensive information about its 
 * properties and the containing file system.
 */

public class FileAttributes {
    
    public static void main(String[] args) {
        // Create a File object representing "test.txt" in the current directory
        File file = new File("test.txt");
        
        // Section 1: Basic File Information
        System.out.println("=== Basic File Information ===");
        
        // Get and display the file's name
        String name = file.getName();
        System.out.println("File name: " + name);
        
        // Get and display the file's size in bytes and kilobytes
        long size = file.length();
        double sizeKB = size / 1024.0;
        System.out.println("File size: " + size + " bytes (" + String.format("%.2f", sizeKB) + " KB)");
        
        // Get and format the last modified timestamp
        long lastModified = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastModifiedStr = (lastModified == 0) ? "Not available" : dateFormat.format(new Date(lastModified));
        System.out.println("Last modified: " + lastModifiedStr);
        
        // Section 2: File Status and Permissions
        System.out.println("\n=== File Status and Permissions ===");
        
        // Check if the file exists in the file system
        boolean exists = file.exists();
        System.out.println("File exists: " + exists);
        
        // Check if the file is a directory
        boolean isDirectory = file.isDirectory();
        System.out.println("Is directory: " + isDirectory);
        
        // Check if the file is a regular file (not a directory)
        boolean isFile = file.isFile();
        System.out.println("Is regular file: " + isFile);
        
        // Check if the file is hidden
        boolean isHidden = file.isHidden();
        System.out.println("Is hidden: " + isHidden);
        
        // Check read permission
        boolean isReadable = file.canRead();
        System.out.println("Is readable: " + isReadable);
        
        // Check write permission
        boolean isWritable = file.canWrite();
        System.out.println("Is writable: " + isWritable);
        
        // Check execute permission
        boolean isExecutable = file.canExecute();
        System.out.println("Is executable: " + isExecutable);
        
        // Section 3: Path Information
        System.out.println("\n=== Path Information ===");
        
        // Get the absolute path of the file
        String absolutePath = file.getAbsolutePath();
        System.out.println("Absolute path: " + absolutePath);
        
        // Get the canonical path (resolves symbolic links)
        try {
            String canonicalPath = file.getCanonicalPath();
            System.out.println("Canonical path: " + canonicalPath);
        } catch (Exception e) {
            System.out.println("Canonical path: Unable to resolve - " + e.getMessage());
        }
        
        // Get the parent directory
        String parent = file.getParent();
        System.out.println("Parent directory: " + (parent != null ? parent : "None (root level)"));
        
        // Section 4: File System Information
        System.out.println("\n=== File System Information ===");
        
        // Get free space in the file system
        long freeSpace = file.getFreeSpace();
        System.out.println("Free space: " + formatSize(freeSpace));
        
        // Get total space in the file system
        long totalSpace = file.getTotalSpace();
        System.out.println("Total space: " + formatSize(totalSpace));
        
        // Get usable space in the file system
        long usableSpace = file.getUsableSpace();
        System.out.println("Usable space: " + formatSize(usableSpace));
    }
    
    // Helper method to format file sizes in a human-readable format
    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        else if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        else return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}