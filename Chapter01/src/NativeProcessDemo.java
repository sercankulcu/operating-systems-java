import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class NativeProcessDemo {

    // Inner class demonstrating ProcessBuilder usage
    static class ProcessBuilderDemo {

        // Method to create and execute a process using ProcessBuilder
        int createAndExecuteProcess(String command, boolean readOutput) throws IOException, InterruptedException {
            // Create a ProcessBuilder with the command split into arguments
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            // Start the process
            Process process = processBuilder.start();

            // If readOutput is true, read and print the process's output and error streams
            if (readOutput) {
                // Read the standard output stream
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    // Read each line and print it to the console
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                // Read the error output stream
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    // Read each line and print it to the standard error stream
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            }

            // Wait for the process to finish, with a 5-second timeout
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            // If the process didn't finish within the timeout, destroy it and return -1
            if (!finished) {
                process.destroy();
                return -1; //timeout
            }

            // Return the process's exit code
            return process.exitValue();
        }
    }

    // Inner class demonstrating Runtime.exec usage
    static class RuntimeExecDemo {
        // Method to create and execute a process using Runtime.exec
        int createAndExecuteProcess(String command, boolean readOutput) throws IOException, InterruptedException {
            // Execute the command using Runtime.exec
            Process process = Runtime.getRuntime().exec(command);

            // If readOutput is true, read and print the process's output and error streams
            if (readOutput) {
                // Read the standard output stream
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    // Read each line and print it to the console
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                // Read the error output stream
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    // Read each line and print it to the standard error stream
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            }
            // Wait for the process to finish, with a 5-second timeout
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            // If the process didn't finish within the timeout, destroy it and return -1
            if (!finished) {
                process.destroy();
                return -1; //timeout
            }
            // Return the process's exit code
            return process.exitValue();
        }
    }

    // Main method to demonstrate process creation and execution
    public static void main(String[] args) throws java.io.IOException, InterruptedException {

        // Create instances of ProcessBuilderDemo and RuntimeExecDemo
        NativeProcessDemo.ProcessBuilderDemo builder = new NativeProcessDemo.ProcessBuilderDemo();
        NativeProcessDemo.RuntimeExecDemo runtime = new NativeProcessDemo.RuntimeExecDemo();

        // Execute ping command using ProcessBuilder and print the output and exit code
        System.out.println("ProcessBuilder Output:");
        int exitCodeBuilder = builder.createAndExecuteProcess("ping -n 3 127.0.0.1", true);
        System.out.println("ProcessBuilder Exit Code: " + exitCodeBuilder);

        // Execute ping command using Runtime.exec and print the output and exit code
        System.out.println("\nRuntime.exec Output:");
        int exitCodeRuntime = runtime.createAndExecuteProcess("ping -n 3 127.0.0.1", true);
        System.out.println("Runtime.exec Exit Code: " + exitCodeRuntime);

        // Execute mspaint command using Runtime.exec and print the exit code (without reading output)
        System.out.println("\nRuntime.exec mspaint:");
        int exitCodeMspaint = runtime.createAndExecuteProcess("mspaint", false);
        System.out.println("Runtime.exec Exit Code: " + exitCodeMspaint);
    }
}