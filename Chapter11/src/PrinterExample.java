import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Arrays;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;

/*
 * This enhanced Java program demonstrates printer functionality using the Java Print Service API:
 * - Prints text to the default printer
 * - Lists available print services
 * - Adds print job attributes (copies, job name)
 * - Includes comprehensive error handling
 * - Supports multiple print attempts
 * 
 * The program uses javax.print package to interact with system printers and provides
 * feedback about the printing process. Note: StringBufferInputStream is deprecated,
 * but kept here for compatibility with the original example.
 */

public class PrinterExample {
    @SuppressWarnings("deprecation")  // Suppress warning for StringBufferInputStream
    public static void main(String[] args) {
        // Section 1: List Available Print Services
        System.out.println("=== Available Print Services ===");
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        if (services.length == 0) {
            System.out.println("No print services available");
            return;
        }
        
        // Display all available printers
        for (int i = 0; i < services.length; i++) {
            System.out.println(i + ": " + services[i].getName());
        }
        
        // Section 2: Select and Verify Default Printer
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            System.err.println("No default print service found");
            return;
        }
        System.out.println("\nDefault printer: " + printService.getName());
        
        // Section 3: Prepare Print Content
        String text = "Hello, world!\n" +
                     "This is a test print job\n" +
                     "Date: " + java.time.LocalDateTime.now();
        InputStream inputStream = null;
        
        try {
            // Convert string to InputStream (using deprecated class for example continuity)
            inputStream = new StringBufferInputStream(text);
            
            // Section 4: Configure Print Job Attributes
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(new Copies(1));  // Number of copies
            attributes.add(new JobName("TestPrint-" + System.currentTimeMillis(), null));
            
            // Section 5: Create and Configure Print Job
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(inputStream, flavor, null);
            DocPrintJob job = printService.createPrintJob();
            
            // Section 6: Execute Print Job
            System.out.println("\nSending print job...");
            job.print(doc, attributes);
            System.out.println("Print job completed successfully");
            
            // Optional: Verify supported document flavors
            System.out.println("\nSupported flavors for this printer:");
            DocFlavor[] flavors = printService.getSupportedDocFlavors();
            Arrays.stream(flavors).forEach(System.out::println);
            
        } catch (PrintException e) {
            // Handle printing-specific errors
            System.err.println("Printing failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Section 7: Cleanup
            if (inputStream != null) {
                try {
                    inputStream.close();
                    System.out.println("Input stream closed");
                } catch (IOException e) {
                    System.err.println("Error closing input stream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        // Section 8: Alternative Modern Approach (using byte array)
        try {
            byte[] byteContent = "Modern print test\nSecond line".getBytes();
            Doc modernDoc = new SimpleDoc(byteContent, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            DocPrintJob modernJob = printService.createPrintJob();
            modernJob.print(modernDoc, null);
            System.out.println("Modern byte array print job completed");
        } catch (PrintException e) {
            System.err.println("Modern print failed: " + e.getMessage());
        }
    }
}