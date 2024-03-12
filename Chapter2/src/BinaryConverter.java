
import java.util.Scanner;

public class BinaryConverter {

	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);

		// Use a while loop to repeat the process
		while (true) {
			// Taking input from the keyboard
			System.out.print("Enter an integer (or type 'exit' to quit): ");
			String userInput = scanner.next();

			// Check if the user wants to exit
			if (userInput.equalsIgnoreCase("exit")) {
				System.out.println("Exiting the program. Goodbye!");
				break;
			}

			try {
				// Convert to binary string
				int inputNumber = Integer.parseInt(userInput);
				String binaryString = convertToBinary(inputNumber);
				Thread.sleep(10000);
				// Print the binary string
				System.out.println("Binary representation: " + binaryString);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid integer or 'exit'.");
			}
		}

		// Close the scanner
		scanner.close();
	}

	// Function to convert an integer to binary string
	private static String convertToBinary(int number) {
		return Integer.toBinaryString(number);
	}
}
