import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * In Java, you can use the File class to perform various operations on files, such as creating, 
 * deleting, renaming, and copying. Here is a sample Java program that demonstrates some of the 
 * file operations that can be performed in Java:
 * 
 * This program creates a new file called "test.txt" in the current directory, renames it to 
 * "test2.txt", copies it to a new file called "test3.txt", and then deletes the new file.
 * */

public class FileOperations {
	
	public static void main(String[] args) {
		
		// Create a new file in the current directory
		File file = new File("test.txt");
		try {
			file.createNewFile();
			System.out.println("File created: " + file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Rename the file
		File file2 = new File("test2.txt");
		boolean renamed = file.renameTo(file2);
		if (renamed) {
			System.out.println("File renamed: " + file2.getName());
		} else {
			System.out.println("File rename failed");
		}

		// Copy the file
		File file3 = new File("test3.txt");
		//FileUtils.copyFile(file2, file3);
		// Create a FileInputStream for the source file
		File sourceFile = new File("test2.txt");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create a FileOutputStream for the destination file
		File destinationFile = new File("test3.txt");
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Copy the contents of the source file to the destination file
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the input and output streams
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("File copied: " + file3.getName());

		// Delete the file
		boolean deleted = file3.delete();
		if (deleted) {
			System.out.println("File deleted: " + file3.getName());
		} else {
			System.out.println("File delete failed");
		}
		
		deleted = file2.delete();
		if (deleted) {
			System.out.println("File deleted: " + file2.getName());
		} else {
			System.out.println("File delete failed");
		}
		
		deleted = file.delete();
		if (deleted) {
			System.out.println("File deleted: " + file.getName());
		} else {
			System.out.println("File delete failed: " + file.getName());
		}
	}
}
