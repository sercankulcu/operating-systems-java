import java.io.File;
import java.io.IOException;

/*
 * Another important concept in operating systems is process management. You can use the 
 * ProcessBuilder class in Java to create and manage operating system processes.
 * 
 * The p.waitFor() method is used to wait for the process to complete and the p.exitValue() 
 * method is used to get the exit code of the process.
 * 
 * */

public class ProcessBuilderExample {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir");
        pb.directory(new File("C:/Users/"));
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            p.waitFor();
            System.out.println("Exit code: " + p.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
