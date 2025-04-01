import java.io.BufferedReader;
import java.io.IOException;
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
            // Start a new process to execute the "dir" command in a specific directory
            Process process = Runtime.getRuntime().exec("cmd /c dir C:\\Users\\"); //windows, change for other OS

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Read the error stream of the process as well
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("ERROR: " + errorLine);
            }

            // Wait for the process to complete and check its exit value
            int exitValue = process.waitFor();
            System.out.println("Exit value: " + exitValue);

            //Example of writing to the process input stream
            //WARNING: dir command does not accept input, other commands do.
            /*
            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            writer.write("some input\n");
            writer.flush();
            writer.close();
            */

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}