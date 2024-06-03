import java.io.File;
import java.text.SimpleDateFormat;

/*
 * In Java, you can use the File class to get various attributes of a file, such as its name, 
 * size, and modification time. Here is a sample Java program that demonstrates how to get some 
 * of the attributes of a file:
 * 
 * This program creates a file object for a file called "test.txt" in the current directory, and 
 * then gets and prints the file's name, size, last modified time, and some other attributes.
 * */

public class FileAttributesExample {
	
	public static void main(String[] args) {
		
		// Create a file object for a file in the current directory
		File file = new File("test.txt");
		// Get the file's name
		String name = file.getName();
		System.out.println("File name: " + name);
		// Get the file's size in bytes
		long size = file.length();
		System.out.println("File size: " + size + " bytes");
		// Get the file's last modified time
		long lastModified = file.lastModified();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastModifiedStr = dateFormat.format(lastModified);
		System.out.println("Last modified: " + lastModifiedStr);
		// Check if the file is a directory
		boolean isDirectory = file.isDirectory();
		System.out.println("Is directory: " + isDirectory);
		// Check if the file is hidden
		boolean isHidden = file.isHidden();
		System.out.println("Is hidden: " + isHidden);
		// Check if the file is readable
		boolean isReadable = file.canRead();
		System.out.println("Is readable: " + isReadable);
		// Check if the file is writable
		boolean isWritable = file.canWrite();
		System.out.println("Is writable: " + isWritable);
	}
}
