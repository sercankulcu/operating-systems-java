import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/*
 * Here is an example of a Java program that demonstrates interprocess communication using pipes:
 * 
 * In this example, the PipedInputStream and PipedOutputStream classes are used to create pipes 
 * for interprocess communication. The PipedInputStream is created first, and the PipedOutputStream 
 * is created using the PipedInputStream as an argument. 
 * 
 * The write method of the PipedOutputStream is used to send a message from the child process 
 * to the parent process, and the read method of the PipedInputStream is used to read the message 
 * in the parent process.
 * */

public class InterprocessCommunicationExample {
    public static void main(String[] args) throws IOException {
        // Create the pipes
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // Create the child process
        new Thread(() -> {
            try {
                // Send message through the pipe
                out.write("Hello from the child process!".getBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Read message from the pipe
        int ch;
        StringBuilder message = new StringBuilder();
        while ((ch = in.read()) != -1) {
            message.append((char) ch);
        }
        in.close();
        System.out.println(message);
    }
}
