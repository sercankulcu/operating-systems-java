
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Here's an example of how you could draw a simple picture in Java using the Graphics2D class from the Java 2D API:
 * 
 * This code creates a JFrame window and a JPanel component. The paintComponent method of the 
 * JPanel component is overridden to perform the actual drawing. The method uses the Graphics2D 
 * class to draw a red oval, a blue rectangle, and a green rounded rectangle.
 * 
 * */

public class PictureDrawingExample extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.red);
    g2d.fillOval(20, 20, 100, 100);

    g2d.setColor(Color.blue);
    g2d.fillRect(140, 20, 100, 100);

    g2d.setColor(Color.green);
    g2d.fillRoundRect(260, 20, 100, 100, 50, 50);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(400, 200));
    frame.add(new PictureDrawingExample());
    frame.pack();
    frame.setVisible(true);
  }
}
