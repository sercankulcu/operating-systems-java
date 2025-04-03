package RPC;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*
 * This class implements the Calculator interface and sets up the RMI server.
 * It extends UnicastRemoteObject to enable remote invocation and binds the
 * service to an RMI registry.
 */
public class CalculatorServer extends UnicastRemoteObject implements Calculator {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -265598959749550578L;

	// Constructor must throw RemoteException due to UnicastRemoteObject
    public CalculatorServer() throws RemoteException {
        super();  // Call the superclass constructor
    }
    
    // Implementation of the add method
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
    
    // Implementation of the subtract method
    @Override
    public int subtract(int a, int b) throws RemoteException {
        return a - b;
    }
    
    // Implementation of the multiply method
    @Override
    public int multiply(int a, int b) throws RemoteException {
        return a * b;
    }
    
    // Implementation of the divide method
    @Override
    public double divide(int a, int b) throws RemoteException {
        if (b == 0) {
            throw new RemoteException("Division by zero is not allowed");
        }
        return (double) a / b;
    }
    
    public static void main(String[] args) {
        try {
            // Create an instance of the server
            CalculatorServer server = new CalculatorServer();
            
            // Create and start the RMI registry on the default port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Bind the server object to the registry with a name "CalculatorService"
            registry.rebind("CalculatorService", server);
            
            System.out.println("Calculator Server is running...");
            System.out.println("Bound to registry as 'CalculatorService' on port 1099");
            
        } catch (RemoteException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}