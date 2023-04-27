import java.awt.Frame;
import java.awt.Label;

/*
 * Here is an example of a Java program that demonstrates the use of Frame:
 * 
 * */

public class WindowExample {
    public static void main(String[] args) {
        // Create a frame with a title
        Frame frame = new Frame("Example Window");

        // Set the size of the frame
        frame.setSize(400, 300);

        // Add a label to the content pane
        frame.add(new Label("Hello, world!"));

        // Show the frame
        frame.setVisible(true);
    }
}
