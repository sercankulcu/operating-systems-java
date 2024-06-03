package rpc;

//RemoteInterface.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
	int add(int a, int b) throws RemoteException;
}
