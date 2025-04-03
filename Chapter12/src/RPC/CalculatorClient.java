package RPC;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

/*
 * This class acts as the RMI client, connecting to the server and invoking
 * remote methods on the Calculator service.
 */
public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Locate the RMI registry running on localhost at port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Lookup the remote object by its registered name
            Calculator calc = (Calculator) registry.lookup("CalculatorService");
            
            System.out.println("Connected to Calculator Server");
            System.out.println("Performing remote calculations:");
            
            // Invoke remote methods and display results
            System.out.println("Addition: 5 + 3 = " + calc.add(5, 3));
            System.out.println("Subtraction: 10 - 4 = " + calc.subtract(10, 4));
            System.out.println("Multiplication: 6 * 7 = " + calc.multiply(6, 7));
            System.out.println("Division: 15 / 2 = " + calc.divide(15, 2));
            
            // Demonstrate error handling with division by zero
            try {
                calc.divide(10, 0);
            } catch (RemoteException e) {
                System.out.println("Expected error: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}