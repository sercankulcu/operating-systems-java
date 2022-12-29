import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/*
 * here is another Java program that demonstrates networking using the java.net package: 
 * a simple HTTP client.
 * 
 * This program opens a connection to the website www.example.com and sends a GET request 
 * to the server using the URLConnection and OutputStreamWriter classes. It then reads and 
 * prints the response from the server
 * */

public class HTTPClient {
  public static void main(String[] args) {
    try {
      // Open a connection to www.example.com
      URL url = new URL("http://www.example.com");
      URLConnection connection = url.openConnection();
      
      // Send a GET request to the server
      connection.setDoOutput(true);
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write("GET / HTTP/1.1\r\n");
      writer.write("Host: www.example.com\r\n");
      writer.write("\r\n");
      writer.flush();
      
      // Read and print the response from the server
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
