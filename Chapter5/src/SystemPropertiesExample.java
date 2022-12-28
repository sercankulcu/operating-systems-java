import java.util.Properties;

/*
 * In Java, you can use the System.getProperties() method to get the system properties as a 
 * Properties object, and the Properties.getProperty() method to get the value of a specific 
 * property. Here is an example of how you can use these methods to get system properties 
 * in Java:
 * */

public class SystemPropertiesExample {
    public static void main(String[] args) {
        // Get the system properties
        Properties properties = System.getProperties();

        // Get the value of the "os.name" property
        String osName = properties.getProperty("os.name");
        System.out.println("OS name: " + osName);

        // Get the value of the "user.name" property
        String userName = properties.getProperty("user.name");
        System.out.println("User name: " + userName);
        
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java version: " + javaVersion);

    }
}
