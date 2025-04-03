import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class demonstrates drawing various shapes using the Graphics2D class from the Java 2D API.
 * It creates a JFrame window containing a custom JPanel where shapes are drawn.
 * The shapes include an oval, rectangle, rounded rectangle, and additional elements like a triangle and text labels.
 */
public class PictureDrawingExample extends JPanel {

    // Serial version UID for serialization compatibility
    private static final long serialVersionUID = 1L;

    // Constants for window dimensions
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;

    // Constants for shape positions and sizes
    private static final int SHAPE_SIZE = 100;
    private static final int SHAPE_SPACING = 20;
    private static final int TEXT_OFFSET = 15;

    /**
     * Constructor for PictureDrawingExample.
     * Sets the background color of the panel.
     */
    public PictureDrawingExample() {
        setBackground(Color.LIGHT_GRAY); // Set a light gray background for better visibility
    }

    /**
     * Overrides the paintComponent method to perform custom drawing.
     * @param g The Graphics object used for drawing
     */
    @Override
    public void paintComponent(Graphics g) {
        // Call the parent class's paintComponent to ensure proper rendering
        super.paintComponent(g);

        // Cast the Graphics object to Graphics2D for advanced drawing features
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a red oval
        g2d.setColor(Color.RED);
        g2d.fillOval(SHAPE_SPACING, SHAPE_SPACING, SHAPE_SIZE, SHAPE_SIZE);
        // Add label for the oval
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Red Oval", SHAPE_SPACING, SHAPE_SPACING + SHAPE_SIZE + TEXT_OFFSET);

        // Draw a blue rectangle
        g2d.setColor(Color.BLUE);
        int rectX = SHAPE_SPACING * 2 + SHAPE_SIZE;
        g2d.fillRect(rectX, SHAPE_SPACING, SHAPE_SIZE, SHAPE_SIZE);
        // Add label for the rectangle
        g2d.setColor(Color.BLACK);
        g2d.drawString("Blue Rectangle", rectX, SHAPE_SPACING + SHAPE_SIZE + TEXT_OFFSET);

        // Draw a green rounded rectangle
        g2d.setColor(Color.GREEN);
        int roundRectX = SHAPE_SPACING * 3 + SHAPE_SIZE * 2;
        g2d.fillRoundRect(roundRectX, SHAPE_SPACING, SHAPE_SIZE, SHAPE_SIZE, 50, 50);
        // Add label for the rounded rectangle
        g2d.setColor(Color.BLACK);
        g2d.drawString("Green Rounded Rect", roundRectX, SHAPE_SPACING + SHAPE_SIZE + TEXT_OFFSET);

        // Draw a yellow triangle below the shapes
        g2d.setColor(Color.YELLOW);
        int[] xPoints = {SHAPE_SPACING, SHAPE_SPACING + SHAPE_SIZE / 2, SHAPE_SPACING + SHAPE_SIZE};
        int[] yPoints = {SHAPE_SPACING * 2 + SHAPE_SIZE, SHAPE_SPACING * 2 + 2 * SHAPE_SIZE, SHAPE_SPACING * 2 + SHAPE_SIZE};
        g2d.fillPolygon(xPoints, yPoints, 3);
        // Add label for the triangle
        g2d.setColor(Color.BLACK);
        g2d.drawString("Yellow Triangle", SHAPE_SPACING * 2, SHAPE_SPACING * 2 + 2 * SHAPE_SIZE + TEXT_OFFSET);

        // Draw a magenta line across the panel
        g2d.setColor(Color.MAGENTA);
        g2d.setStroke(new java.awt.BasicStroke(2)); // Set line thickness
        g2d.drawLine(0, WINDOW_HEIGHT - 100, WINDOW_WIDTH, WINDOW_HEIGHT - 150);
        // Add label for the line
        g2d.setColor(Color.BLACK);
        g2d.drawString("Magenta Line", WINDOW_WIDTH / 2 - 30, WINDOW_HEIGHT - 130);
    }

    /**
     * Main method to set up and display the JFrame window.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create the main window
        JFrame frame = new JFrame("Shape Drawing Example");
        
        // Set the operation when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set the preferred size of the window
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        
        // Create an instance of our custom panel and add it to the frame
        PictureDrawingExample panel = new PictureDrawingExample();
        frame.add(panel);
        
        // Pack the frame to fit its contents
        frame.pack();
        
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
        
        // Make the window visible
        frame.setVisible(true);
    }
}