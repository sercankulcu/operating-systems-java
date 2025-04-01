
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketThreadExample {

	public static void main(String[] args) throws InterruptedException {
		
		Thread inputThread = new Thread(() -> {
			try (Scanner scanner = new Scanner(System.in)) {
				try (Socket clientSocket = new Socket("localhost", 12345);
						ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

					while (true) {
						System.out.println("Enter an integer (enter 0 to exit): ");
						int userInput = scanner.nextInt();
						out.writeObject(userInput);
						if (userInput == 0) {
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		Thread binaryConversionThread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(12345);
					Socket clientSocket = serverSocket.accept();
					Socket clientSocket2 = new Socket("localhost", 12346);
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
					ObjectOutputStream out = new ObjectOutputStream(clientSocket2.getOutputStream())) {
				while (true) {
					int userInput = (int) in.readObject();
					Thread.sleep(10000);
					String binaryString = Integer.toBinaryString(userInput);
					out.writeObject(binaryString);
				}
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread printThread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(12346);
					Socket clientSocket = serverSocket.accept();
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
				while (true) {
					String binaryString = (String) in.readObject();
					System.out.println("Binary representation: " + binaryString);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		printThread.start();
		Thread.sleep(1000);
		binaryConversionThread.start();
		Thread.sleep(1000);
		inputThread.start();
	}
}
