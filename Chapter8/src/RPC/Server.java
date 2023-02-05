package RPC;

//Server.java
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements RemoteInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Server() throws RemoteException {
		super();
	}

	public int add(int a, int b) throws RemoteException {
		return a + b;
	}

	public static void main(String[] args) {
		try {
			Naming.rebind("//localhost/MyServer", new Server());
			System.err.println("Server ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
