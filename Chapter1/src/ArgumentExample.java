/*
 * Here is an example of a Java program that takes arguments from the console and performs 
 * an operation:
 * 
 * To run this program, you can use the following command:
 * java ArgumentExample 10 20
 * */

public class ArgumentExample {
    public static void main(String[] args) {
        // Check if at least two arguments were passed
        if (args.length < 2) {
            System.out.println("Please provide at least two arguments.");
            return;
        }
        // Parse the first argument as an integer
        int num1 = Integer.parseInt(args[0]);
        // Parse the second argument as an integer
        int num2 = Integer.parseInt(args[1]);
        // Perform the addition operation
        int sum = num1 + num2;
        // Print the result
        System.out.println(num1 + " + " + num2 + " = " + sum);
    }
}
