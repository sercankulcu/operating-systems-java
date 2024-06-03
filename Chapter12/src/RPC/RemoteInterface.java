package RPC;

//RemoteInterface.java

/*
 * javac *.java
 * rmic RemoteInterface
 * rmiregistry 5000
 * java Server
 * java Client  
 * 
 * */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
 int add(int a, int b) throws RemoteException;
}
