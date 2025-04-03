import java.awt.Image;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.io.File;
import java.io.IOException;

/**
 * This class demonstrates how to load and display an image in a Swing window
 * with error handling and basic window configuration
 */
public class ImageLoadingExample {
    
    // Constants for window dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        try {
            // Attempt to read the image file from the specified path
            Image image = ImageIO.read(new File("input.jpg"));
            
            // Check if image loaded successfully
            if (image == null) {
                throw new IOException("Failed to load image: unsupported format or file not found");
            }

            // Create a JLabel with the loaded image
            JLabel label = new JLabel(new ImageIcon(image));
            
            // Create a scroll pane to handle large images
            JScrollPane scrollPane = new JScrollPane(label);
            
            // Create the main window (JFrame)
            JFrame frame = new JFrame("Image Viewer");
            
            // Set the operation when window is closed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add the scroll pane to the frame
            frame.add(scrollPane);
            
            // Set preferred window size
            frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            
            // Adjust frame size to fit content
            frame.pack();
            
            // Center the window on screen
            frame.setLocationRelativeTo(null);
            
            // Make the window visible
            frame.setVisible(true);
            
        } catch (IOException e) {
            // Handle any IO exceptions (file not found, invalid format, etc.)
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}