package IPC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * In Java, you can use the ProcessBuilder class to start a new process and redirect its standard 
 * input and output to execute shell commands on Windows. Here is an example of how to use pipes 
 * to execute the command echo | dir on Windows using the ProcessBuilder class:
 * */

public class PipeExample {
	
    public static void main(String[] args) {
    	
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo", "Hello, World!" , "|", "dir");
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
