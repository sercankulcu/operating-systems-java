import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class NativeProcessDemo {

    static class ProcessBuilderDemo {

        int createAndExecuteProcess(String command, boolean readOutput) throws IOException, InterruptedException {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();

            if (readOutput) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            }

            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                return -1; //timeout
            }

            return process.exitValue();
        }
    }

    static class RuntimeExecDemo {
        int createAndExecuteProcess(String command, boolean readOutput) throws IOException, InterruptedException {
            Process process = Runtime.getRuntime().exec(command);

            if (readOutput) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            }
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                return -1; //timeout
            }
            return process.exitValue();
        }
    }

    public static void main(String[] args) throws java.io.IOException, InterruptedException {
    	
        NativeProcessDemo.ProcessBuilderDemo builder = new NativeProcessDemo.ProcessBuilderDemo();
        NativeProcessDemo.RuntimeExecDemo runtime = new NativeProcessDemo.RuntimeExecDemo();

        System.out.println("ProcessBuilder Output:");
        int exitCodeBuilder = builder.createAndExecuteProcess("ping -n 3 127.0.0.1", true);
        System.out.println("ProcessBuilder Exit Code: " + exitCodeBuilder);

        System.out.println("\nRuntime.exec Output:");
        int exitCodeRuntime = runtime.createAndExecuteProcess("ping -n 3 127.0.0.1", true);
        System.out.println("Runtime.exec Exit Code: " + exitCodeRuntime);

        System.out.println("\nRuntime.exec mspaint:");
        int exitCodeMspaint = runtime.createAndExecuteProcess("mspaint", false);
        System.out.println("Runtime.exec Exit Code: " + exitCodeMspaint);
    }
}