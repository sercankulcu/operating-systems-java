import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * here is another Java program that demonstrates an operating system feature: networking 
 * using the java.net package.
 * 
 * This program starts a server on port 8080 and waits for a client to connect. When a client 
 * connects, it reads a message from the client using a BufferedReader and prints it out. It 
 * then sends a message back to the client using a PrintWriter.
 * */

public class ServerSocketExample {
  public static void main(String[] args) {
    try {
      // Start a server on port 8080
      ServerSocket serverSocket = new ServerSocket(8080);
      
      // Accept a client connection
      Socket clientSocket = serverSocket.accept();
      
      // Read and print the client's message
      BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String message = reader.readLine();
      System.out.println("Received: " + message);
      
      // Send a message back to the client
      PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
      writer.println("Hello from the server!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
