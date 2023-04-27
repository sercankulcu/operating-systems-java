
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Here's a simple Java code that demonstrates how to load an image from a file and display it in a JFrame window:
 * 
 * */

public class ImageLoadingExample {

  public static void main(String[] args) {
    try {
      Image image = ImageIO.read(new File("input.jpg"));
      JLabel label = new JLabel(new ImageIcon(image));
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(label);
      frame.pack();
      frame.setVisible(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
