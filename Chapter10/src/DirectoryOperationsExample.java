import java.io.File;

/*
 * In Java, you can use the File class to perform various operations on directories, such as 
 * creating, deleting, and listing the contents of a directory. Here is a sample Java program 
 * that demonstrates some of the directory operations that can be performed in Java:
 * 
 * This program creates a new directory called "new_directory" in the current directory, lists 
 * the contents of the current directory, and then deletes the new directory.
 * 
 * */

public class DirectoryOperationsExample {
    public static void main(String[] args) {
        // Create a new directory in the current directory
        File directory = new File("new_directory");
        boolean created = directory.mkdir();
        if (created) {
            System.out.println("Directory created: " + directory.getName());
        } else {
            System.out.println("Directory creation failed");
        }

        // List the contents of the current directory
        File currentDirectory = new File(".");
        String[] fileList = currentDirectory.list();
        System.out.println("Contents of current directory:");
        for (String fileName : fileList) {
            System.out.println(fileName);
        }

        // Delete the new directory
        boolean deleted = directory.delete();
        if (deleted) {
            System.out.println("Directory deleted: " + directory.getName());
        } else {
            System.out.println("Directory delete failed");
        }
    }
}
