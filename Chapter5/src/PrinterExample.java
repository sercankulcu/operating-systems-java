import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/*
 * Here is an example of a Java program that demonstrates the use of a printer:
 * 
 * */

public class PrinterExample {
    @SuppressWarnings("deprecation")
		public static void main(String[] args) {
        // Get the default print service
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

        // Convert the string to an InputStream
        String text = "Hello, world!";
        InputStream inputStream = new StringBufferInputStream(text);

        // Create a Doc and a DocPrintJob
        Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        DocPrintJob job = printService.createPrintJob();

        // Print the document
        try {
            job.print(doc, null);
        } catch (PrintException e) {
            e.printStackTrace();
        } finally {
            // Close the InputStream
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
