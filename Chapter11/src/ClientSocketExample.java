import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * here is a Java program that implements the client side of the simple networking example 
 * I provided earlier:
 * 
 * This program connects to a server running on localhost at port 8080 using the Socket class. 
 * It then sends a message to the server using a PrintWriter and reads and prints the server's 
 * response using a BufferedReader.
 * */

public class ClientSocketExample {
  public static void main(String[] args) {
    try {
      // Connect to the server at localhost:8080
      Socket socket = new Socket("localhost", 8080);
      
      // Send a message to the server
      PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
      writer.println("Hello from the client!");
      
      // Read and print the server's response
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String message = reader.readLine();
      System.out.println("Received: " + message);
      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
