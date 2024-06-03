package rpc;

//Client.java
import java.rmi.Naming;

public class Client {

	public static void main(String[] args) {
		try {
			RemoteInterface stub = (RemoteInterface) Naming.lookup("//localhost/MyServer");
			System.out.println("Addition: " + stub.add(3, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
