import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * here is another Java program that demonstrates an operating system feature: 
 * process management using the java.lang.Process class.
 * 
 * In this program, we use the exec method of the Runtime class to start a new process that 
 * executes the ls command. This command lists the contents of the current directory.
 * */

public class ProcessExample {
  public static void main(String[] args) {
    try {
      // Start a new process to execute the "ls" command
      Process process = Runtime.getRuntime().exec("dir");
      
      // Read the output of the process
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      
      // Wait for the process to complete and check its exit value
      int exitValue = process.waitFor();
      System.out.println("Exit value: " + exitValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
