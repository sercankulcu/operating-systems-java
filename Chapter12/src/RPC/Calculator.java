package RPC;
import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * This interface defines the remote methods that can be invoked by the client.
 * It must extend java.rmi.Remote, and all methods must throw RemoteException.
 */
public interface Calculator extends Remote {
    // Adds two numbers remotely
    int add(int a, int b) throws RemoteException;
    
    // Subtracts b from a remotely
    int subtract(int a, int b) throws RemoteException;
    
    // Multiplies two numbers remotely
    int multiply(int a, int b) throws RemoteException;
    
    // Divides a by b remotely (returns double to handle fractions)
    double divide(int a, int b) throws RemoteException;
}