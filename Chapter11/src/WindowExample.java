import java.awt.Frame;
import java.awt.Label;
import java.awt.Button;
import java.awt.TextField;
import java.awt.Checkbox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Color;

/*
 * This enhanced Java program demonstrates the use of AWT Frame with:
 * - Multiple UI components (Label, Button, TextField, Checkbox)
 * - Event handling for window closing and button clicks
 * - Layout management
 * - Basic customization (colors, positioning)
 * 
 * The program creates a window with interactive elements and proper cleanup
 * when the window is closed. AWT (Abstract Window Toolkit) is Java's original
 * GUI toolkit, suitable for basic desktop applications.
 */

public class WindowExample {
    // Constants for window dimensions
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    
    public static void main(String[] args) {
        // Create a frame with a title
        Frame frame = new Frame("Example Window");
        
        // Set the size of the frame (width, height in pixels)
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Set a layout manager (FlowLayout for simple sequential arrangement)
        frame.setLayout(new FlowLayout());
        
        // Set background color for better visibility
        frame.setBackground(Color.LIGHT_GRAY);
        
        // Section 1: Add UI Components
        // Add a label with centered alignment
        Label greetingLabel = new Label("Hello, world!", Label.CENTER);
        frame.add(greetingLabel);
        
        // Add a text field for user input
        TextField inputField = new TextField("Type here", 20);
        frame.add(inputField);
        
        // Add a button with action listener
        Button clickButton = new Button("Click Me!");
        frame.add(clickButton);
        
        // Add a checkbox for toggle option
        Checkbox toggleBox = new Checkbox("Toggle Option", true);
        frame.add(toggleBox);
        
        // Add a status label to show updates
        Label statusLabel = new Label("Status: Ready");
        frame.add(statusLabel);
        
        // Section 2: Add Event Handling
        // Handle window closing event
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Clean up and exit when window is closed
                frame.dispose();
                System.exit(0);
            }
        });
        
        // Add action listener for button click
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update status label with text field content when button is clicked
                String inputText = inputField.getText();
                statusLabel.setText("Status: Button clicked! Input: " + inputText);
                
                // Change background color based on checkbox state
                if (toggleBox.getState()) {
                    frame.setBackground(Color.DARK_GRAY);
                } else {
                    frame.setBackground(Color.LIGHT_GRAY);
                }
            }
        });
        
        // Section 3: Final Window Configuration
        // Center the window on screen (approximate)
        frame.setLocationRelativeTo(null);
        
        // Make the frame visible
        frame.setVisible(true);
        
        // Optional: Print window info to console
        System.out.println("Window created with size: " + 
                          WINDOW_WIDTH + "x" + WINDOW_HEIGHT);
        System.out.println("Components added: Label, TextField, Button, Checkbox");
    }
}