package IPC;

/*
 * Here is an example of a Java program that demonstrates how to use sockets for IPC between 
 * two separate processes:
 * 
 * In this example, the server creates a ServerSocket on port 8000 and waits for incoming 
 * connections. When a client connects, the server sends a message "Hello, client!" using a 
 * PrintWriter object. On the other side, the client creates a Socket and connects to the server 
 * on port 8000. Then it reads the message sent by the server using a BufferedReader object.
 * */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
    public static void main(String[] args) {
    	
        try (ServerSocket server = new ServerSocket(8000)) {
            while (true) {
                try (Socket socket = server.accept()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Hello, client!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
