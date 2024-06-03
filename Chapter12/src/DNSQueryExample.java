
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSQueryExample {

	public static void main(String[] args) {

		try {
			InetAddress address = InetAddress.getByName("www.google.com");
			System.out.println("Hostname: " + address.getHostName());
			System.out.println("IP Address: " + address.getHostAddress());
		} catch (UnknownHostException e) {
			System.out.println("Unable to resolve hostname.");
		}
	}
}
