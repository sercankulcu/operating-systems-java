import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Here is a simple Java web server that listens to incoming HTTP requests and sends back a 
 * response:
 * 
 * This code creates a ServerSocket on port 8080, which listens for incoming connections. When 
 * a client connects, it reads the HTTP request and sends back a simple response with the text 
 * "Hello World". The response includes the necessary HTTP headers to indicate that the request 
 * was successful (status code 200 OK) and the content type (text/plain).
 * 
 * */

public class SimpleWebServer {
  public static void main(String[] args) throws IOException {
    @SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8080);
    System.out.println("Listening for connections on port 8080");
    
    while (true) {
      Socket client = server.accept();
      System.out.println("Accepted connection from " + client.getInetAddress());
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);

      String request = in.readLine();
      System.out.println("Received request: " + request);
      
      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: text/plain");
      out.println();
      out.println("Hello World");
      out.flush();
      
      client.close();
    }
  }
}
