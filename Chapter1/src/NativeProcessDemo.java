
import java.io.IOException;

public class NativeProcessDemo {

    static class ProcessBuilderDemo {

        int createAndExecuteProcess() throws IOException, InterruptedException {
            ProcessBuilder processBuilder = new ProcessBuilder("mspaint");
            Process process = processBuilder.start();
            Thread.sleep(1000);
//            process.destroy();
            Thread.sleep(1000);
            process.waitFor();
            return process.exitValue();
        }

    }

    static class RuntimeExecDemo {
        int createAndExecuteProcess() throws IOException, InterruptedException {
            Process process = Runtime.getRuntime().exec("mspaint");
            Thread.sleep(1000);
//            process.destroy();
            Thread.sleep(1000);
            process.waitFor();
            return process.exitValue();
        }
    }

    public static void main(String[] args) throws java.io.IOException, InterruptedException {
        System.out.println(
            //new NativeProcessDemo.ProcessBuilderDemo().createAndExecuteProcess()
            new NativeProcessDemo.RuntimeExecDemo().createAndExecuteProcess()
        );
    }
}