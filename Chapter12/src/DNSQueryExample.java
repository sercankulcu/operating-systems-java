import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * This enhanced Java program demonstrates DNS querying using the java.net package:
 * - Resolves hostnames to IP addresses
 * - Supports multiple hostname queries
 * - Differentiates between IPv4 and IPv6 addresses
 * - Displays additional host information
 * - Includes timing and error handling
 * 
 * The program uses InetAddress to perform DNS lookups and provides detailed
 * information about the resolved hosts.
 */

public class DNSQueryExample {
	// Constant for default hostname
	private static final String DEFAULT_HOST = "www.google.com";

	public static void main(String[] args) {
		// Array of hostnames to query (can be extended)
		String[] hosts = {DEFAULT_HOST, "www.github.com", "localhost"};

		// Record start time for performance measurement
		long startTime = System.currentTimeMillis();

		// Section 1: Basic DNS Query
		System.out.println("=== Basic DNS Query ===");
		performDNSQuery(DEFAULT_HOST);

		// Section 2: Multiple Host Queries
		System.out.println("\n=== Multiple Host Queries ===");
		for (String host : hosts) {
			System.out.println("Querying: " + host);
			performDNSQuery(host);
			System.out.println("------------------------");
		}

		// Section 3: Get All Addresses for a Host
		System.out.println("\n=== All Addresses for " + DEFAULT_HOST + " ===");
		try {
			InetAddress[] allAddresses = InetAddress.getAllByName(DEFAULT_HOST);
			System.out.println("Total addresses found: " + allAddresses.length);
			for (InetAddress addr : allAddresses) {
				displayAddressDetails(addr);
			}
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve all addresses for " + DEFAULT_HOST + ": " + e.getMessage());
		}

		// Section 4: Local Host Information
		System.out.println("\n=== Local Host Information ===");
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			displayAddressDetails(localHost);
		} catch (UnknownHostException e) {
			System.err.println("Unable to get local host: " + e.getMessage());
		}

		// Section 5: Display Query Statistics
		printQueryStatistics(startTime, hosts.length);
	}

	/*
	 * Performs a DNS query for a given hostname
	 * @param hostname The hostname to resolve
	 */
	private static void performDNSQuery(String hostname) {
		try {
			InetAddress address = InetAddress.getByName(hostname);
			displayAddressDetails(address);
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve hostname '" + hostname + "': " + e.getMessage());
		}
	}

	/*
	 * Displays detailed information about an InetAddress
	 * @param address The InetAddress object to analyze
	 */
	private static void displayAddressDetails(InetAddress address) {
		System.out.println("Hostname: " + address.getHostName());
		System.out.println("IP Address: " + address.getHostAddress());
		System.out.println("Canonical Hostname: " + address.getCanonicalHostName());

		// Check IP version
		if (address instanceof Inet4Address) {
			System.out.println("IP Version: IPv4");
		} else if (address instanceof Inet6Address) {
			System.out.println("IP Version: IPv6");
		}

		// Get the raw bytes of the IP address
		byte[] ipBytes = address.getAddress();

		// Convert to unsigned values and display
		int[] unsignedBytes = new int[ipBytes.length];
		for (int i = 0; i < ipBytes.length; i++) {
			unsignedBytes[i] = ipBytes[i] & 0xFF;  // Convert signed byte to unsigned int
		}
		System.out.println("IP Bytes (unsigned): " + Arrays.toString(unsignedBytes));

		// Check reachability (simple test, may require network permissions)
		try {
			boolean reachable = address.isReachable(2000);  // 2-second timeout
			System.out.println("Reachable: " + reachable);
		} catch (IOException e) {
			System.out.println("Reachability test failed: " + e.getMessage());
		}
	}

	/*
	 * Prints statistics about the DNS queries
	 * @param startTime The time when queries began
	 * @param queryCount The number of hosts queried
	 */
	private static void printQueryStatistics(long startTime, int queryCount) {
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000.0;

		System.out.println("\n=== Query Statistics ===");
		System.out.println("Total hosts queried: " + queryCount);
		System.out.printf("Total duration: %.3f seconds%n", duration);
		System.out.println("Average time per query: " + 
				String.format("%.3f", duration / queryCount) + " seconds");
		System.out.println("Timestamp: " + LocalDateTime.now()
		.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
}