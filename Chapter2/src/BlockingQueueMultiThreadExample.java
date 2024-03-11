
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueMultiThreadExample {

	private static BlockingQueue<Integer> inputQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<String> binaryQueue = new LinkedBlockingQueue<>();

	public static void main(String[] args) {
		// Thread for taking input from the keyboard
		Thread inputThread = new Thread(() -> {
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					try {
						System.out.println("Enter an integer (enter 0 to exit): ");
						int userInput = scanner.nextInt();
						inputQueue.put(userInput);

						if (userInput == 0) {
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Thread for converting the input to a binary string
		Thread binaryConversionThread = new Thread(() -> {
			while (true) {
				try {
					int userInput = inputQueue.take();
					if (userInput == 0) {
						break; // Exit thread when user enters 0
					}
					Thread.sleep(10000);
					String binaryString = Integer.toBinaryString(userInput);
					binaryQueue.put(binaryString);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// Thread for printing the binary string
		Thread printThread = new Thread(() -> {
			while (true) {
				try {
					String binaryString = binaryQueue.take();
					System.out.println("Binary representation: " + binaryString);

					if (binaryString.equals("0")) {
						break; // Exit thread when the binary string is "0"
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// Start the threads
		inputThread.start();
		binaryConversionThread.start();
		printThread.start();
	}
}
