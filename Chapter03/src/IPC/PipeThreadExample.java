package IPC;
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

public class PipeThreadExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create the pipes
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);

        // Create the child process (writer)
        Thread writerThread = new Thread(() -> {
            try {
                // Send multiple messages with delays
                for (int i = 0; i < 5; i++) {
                    String message = "Message " + i + " from child process!\n";
                    out.write(message.getBytes());
                    Thread.sleep(500); // Simulate some processing time
                }
                out.write("END".getBytes()); //Signal the end of transmission.
                out.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Create the parent process (reader)
        Thread readerThread = new Thread(() -> {
            try {
                int ch;
                StringBuilder message = new StringBuilder();
                System.out.println("Received char: ");
                while (true) {
                    ch = in.read();
                    if (ch == -1) {
                        break;
                    }
                    message.append((char) ch);
                    System.out.print((char)ch);

                    if(message.toString().endsWith("END")){
                        message.delete(message.length()-3, message.length()); //remove end signal.
                        break;
                    }

                }
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Start the threads
        writerThread.start();
        readerThread.start();

        // Wait for the threads to finish
        writerThread.join();
        readerThread.join();

        System.out.println("\nCommunication complete.");
    }
}