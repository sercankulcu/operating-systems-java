import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * This enhanced Java program demonstrates HTTP client functionality using java.net package:
 * - Connects to a specified URL
 * - Supports both GET and POST requests
 * - Displays response headers
 * - Includes timeout configuration
 * - Provides detailed error handling with error stream support
 * - Tracks request timing
 * 
 * The program fixes the HTTP 405 error by simplifying the GET request and handling error responses.
 */

public class HTTPClient {
    // Constants for configuration
    private static final String TARGET_URL = "https://sercankulcu.github.io";
    private static final int TIMEOUT_MS = 5000;  // 5 seconds timeout
    
    public static void main(String[] args) {
        // Record start time for performance measurement
        long startTime = System.currentTimeMillis();
        
        // Section 1: Initialize Connection Variables
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStreamWriter writer = null;
        
        try {
            // Create URL object
            URL url = new URL(TARGET_URL);
            
            // Section 2: Send GET Request
            System.out.println("=== Sending GET Request ===");
            connection = (HttpURLConnection) url.openConnection();
            configureConnection(connection, "GET");
            sendGetRequest(connection);  // Simplified GET request
            
            // Section 3: Read and Display Response
            System.out.println("\n=== Server Response ===");
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                System.out.println("Error response received (HTTP " + responseCode + ")");
            }
            
            String line;
            int lineCount = 0;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null && lineCount < 10) {
                System.out.println(line);
                response.append(line).append("\n");
                lineCount++;
            }
            if (lineCount >= 10) {
                System.out.println("... (Response truncated to 10 lines)");
            }
            
            // Section 4: Display Response Headers
            System.out.println("\n=== Response Headers ===");
            Map<String, java.util.List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, java.util.List<String>> entry : headers.entrySet()) {
                System.out.printf("%s: %s%n", entry.getKey(), entry.getValue());
            }
            
            // Section 5: Display Request Info
            printRequestInfo(connection, startTime);
            
            // Section 6: Send POST Request with Fresh Connection
            System.out.println("\n=== Sending POST Request ===");
            connection.disconnect();  // Close previous connection
            connection = (HttpURLConnection) url.openConnection();
            configureConnection(connection, "POST");
            sendPostRequest(connection, "Sample POST data");
            
        } catch (IOException e) {
            // Handle network-related errors
            System.err.println("Network error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Section 7: Cleanup Resources
            cleanup(connection, reader, writer);
        }
    }
    
    /*
     * Configures common connection properties
     * @param connection The HttpURLConnection to configure
     * @param method The HTTP method (GET or POST)
     */
    private static void configureConnection(HttpURLConnection connection, String method) 
            throws IOException {
        connection.setRequestMethod(method);
        connection.setDoOutput(method.equals("POST"));  // Only enable output for POST
        connection.setDoInput(true);   // Enable input for reading response
        connection.setConnectTimeout(TIMEOUT_MS);  // Set connection timeout
        connection.setReadTimeout(TIMEOUT_MS);     // Set read timeout
        connection.setRequestProperty("User-Agent", "Java HTTP Client/1.0");
    }
    
    /*
     * Sends a GET request to the server
     * @param connection The HttpURLConnection to use
     */
    private static void sendGetRequest(HttpURLConnection connection) throws IOException {
        // No manual writing needed; HttpURLConnection handles GET automatically
        connection.connect();  // Explicitly connect (optional, as getInputStream() will do this)
    }
    
    /*
     * Sends a POST request with data to the server
     * @param connection The HttpURLConnection to use
     * @param postData The data to send in the POST request
     */
    private static void sendPostRequest(HttpURLConnection connection, String postData) 
            throws IOException {
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
        
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(postData);
            writer.flush();
            System.out.println("POST request sent with data: " + postData);
            System.out.println("Response code: " + connection.getResponseCode());
            
            // Read POST response
            BufferedReader postReader = new BufferedReader(
                connection.getResponseCode() < 400 ? 
                new InputStreamReader(connection.getInputStream()) : 
                new InputStreamReader(connection.getErrorStream())
            );
            String postLine;
            while ((postLine = postReader.readLine()) != null) {
                System.out.println(postLine);
            }
            postReader.close();
        }
    }
    
    /*
     * Prints request information and timing
     * @param connection The HttpURLConnection used
     * @param startTime The time when the request began
     */
    private static void printRequestInfo(HttpURLConnection connection, long startTime) 
            throws IOException {
        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;
        
        System.out.println("\n=== Request Information ===");
        System.out.println("URL: " + TARGET_URL);
        System.out.println("Response code: " + connection.getResponseCode());
        System.out.println("Response message: " + connection.getResponseMessage());
        System.out.printf("Request duration: %.3f seconds%n", duration);
        System.out.println("Timestamp: " + LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /*
     * Cleans up resources used by the connection
     * @param connection The HttpURLConnection to close
     * @param reader The BufferedReader to close
     * @param writer The OutputStreamWriter to close
     */
    private static void cleanup(HttpURLConnection connection, BufferedReader reader, 
                              OutputStreamWriter writer) {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (connection != null) connection.disconnect();
            System.out.println("\nResources cleaned up successfully");
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}