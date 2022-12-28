import java.io.*;

/*
 * Here is a sample Java program that demonstrates some of the different file types:
 * 
 * This program creates a file called "test.txt" in the current directory, writes some text to 
 * the file, and then reads the contents of the file. It then creates a directory called "test" 
 * and creates a file called "test2.txt" in the new directory. It writes some text to the second 
 * file and reads the contents of the second file. Finally, it deletes the two files and the 
 * directory.
 * */

public class FileTypesExample {
    public static void main(String[] args) {
        // Create a file in the current directory
        File file = new File("test.txt");
        try {
            // Write some text to the file
            PrintWriter writer = new PrintWriter(file);
            writer.println("This is a test file.");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a directory in the current directory
        File dir = new File("test");
        dir.mkdir();

        // Create a file in the new directory
        File file2 = new File(dir, "test2.txt");
        try {
            // Write some text to the file
            PrintWriter writer = new PrintWriter(file2);
            writer.println("This is another test file.");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the contents of the first file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the contents of the second file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file2));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the first file
        file.delete();

        // Delete the second file
        file2.delete();

        // Delete the directory
        dir.delete();
    }
}
