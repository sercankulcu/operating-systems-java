import java.util.Properties;
import java.util.Set;
import java.util.Enumeration;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * This enhanced Java program demonstrates working with system properties using the Properties class:
 * - Retrieves specific system properties
 * - Lists all available properties
 * - Adds custom properties
 * - Saves properties to a file
 * - Includes error handling and formatting
 * 
 * System properties provide information about the current Java runtime environment,
 * operating system, and user settings. This program showcases various ways to access
 * and manipulate these properties.
 */

public class SystemPropertiesExample {
    
    // Constant for output file name
    private static final String OUTPUT_FILE = "system_properties.txt";
    
    public static void main(String[] args) {
        // Get the system properties object
        Properties properties = System.getProperties();
        
        // Section 1: Display Common System Properties
        System.out.println("=== Common System Properties ===");
        
        // Get and display operating system name
        String osName = properties.getProperty("os.name", "Not available");
        System.out.println("OS name: " + osName);
        
        // Get and display operating system version
        String osVersion = properties.getProperty("os.version", "Not available");
        System.out.println("OS version: " + osVersion);
        
        // Get and display current user name
        String userName = properties.getProperty("user.name", "Unknown");
        System.out.println("User name: " + userName);
        
        // Get and display Java version
        String javaVersion = properties.getProperty("java.version", "Unknown");
        System.out.println("Java version: " + javaVersion);
        
        // Get and display Java home directory
        String javaHome = properties.getProperty("java.home", "Not specified");
        System.out.println("Java home: " + javaHome);
        
        // Get and display user home directory
        String userHome = properties.getProperty("user.home", "Not specified");
        System.out.println("User home: " + userHome);
        
        // Section 2: Add Custom Property
        System.out.println("\n=== Custom Property ===");
        
        // Add a custom property with current timestamp
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        properties.setProperty("program.run.time", timestamp);
        System.out.println("Custom property added - Run time: " + timestamp);
        
        // Section 3: List All Properties
        System.out.println("\n=== All System Properties ===");
        
        // Get all property names
        Set<String> propertyNames = properties.stringPropertyNames();
        System.out.println("Total properties: " + propertyNames.size());
        
        // Display a subset of properties (first 10) to avoid overwhelming output
        int count = 0;
        for (String key : propertyNames) {
            if (count >= 10) {
                System.out.println("(Showing first 10 properties only)");
                break;
            }
            String value = properties.getProperty(key);
            System.out.printf("%-30s : %s%n", key, value);
            count++;
        }
        
        // Alternative way using Enumeration
        System.out.println("\nSample using Enumeration:");
        Enumeration<?> keys = properties.propertyNames();
        count = 0;
        while (keys.hasMoreElements() && count < 5) {
            String key = (String) keys.nextElement();
            System.out.printf("%-30s : %s%n", key, properties.getProperty(key));
            count++;
        }
        
        // Section 4: Save Properties to File
        System.out.println("\n=== Saving Properties to File ===");
        
        try (FileOutputStream fos = new FileOutputStream(OUTPUT_FILE)) {
            // Save all properties to a file with a comment
            properties.store(fos, "System Properties saved on " + timestamp);
            System.out.println("Properties successfully saved to " + OUTPUT_FILE);
        } catch (IOException e) {
            System.err.println("Error saving properties to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}