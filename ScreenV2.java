package PP14;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ScreenV2 extends JPanel implements ActionListener {

	Random rand = new Random();

	// Size of image
	int wid = 1280, hig = 853; 

	// Starting number of bubbles
	int n = 700;

	boolean grow = true;
	Map<Integer, Point> plist = new HashMap<Integer, Point>(); // Center of bubbles
	Map<Integer, Integer> rlis = new HashMap<Integer, Integer>(); // Radius of bubbles
	Map<Integer, Boolean> growing = new HashMap<Integer, Boolean>(); // State of bubbles
	Map<Integer, Color> color = new HashMap<Integer, Color>(); // Color of bubbles

	private Timer timer;
	private int delay = 50;

	// image
	File file = new File("src/PP14/christmas-elf-4679016_1280.jpg");
	private BufferedImage image;

	public ScreenV2() {
		// load image
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// initialize timer
		timer = new Timer(delay, this);
		timer.start();
		
		// initialize first n bubbles
		for (int i = 0; i < n; i++) {
			newBubble(i);
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);

		// Loop through all bubble pairs to see if they are touching something
		for (int i = 0; i < n; i++) {
			
			// check if the bubble is touch the walls
			if (growing.get(i)) {
				if (plist.get(i).x + rlis.get(i) / 2 >= wid || plist.get(i).x - rlis.get(i) / 2 <= 0
						|| plist.get(i).y + rlis.get(i) / 2 >= hig || plist.get(i).y - rlis.get(i) / 2 <= 0) {
					growing.put(i, false);

					// Create a new bubble if one is touching the wall
					newBubble(n);
				}
			}
			
			// checks if bubbles are touching each other
			for (int j = 0; j < n; j++) {

				// indexs are not the same and both are growing
				if (i != j && (growing.get(i) || growing.get(j))) {
					// The x and y distance from the orgins of the bubbles
					double dx = Math.abs((plist.get(i).x) - (plist.get(j).x));
					double dy = Math.abs((plist.get(i).y) - (plist.get(j).y));

					// The hypotenuse of the x and y distances 
					double maxDistance2 = (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
					
					// checks if the sum of the radii is greater than the hypotenuse
					if (maxDistance2 < ((rlis.get(i) / 2) + (rlis.get(j) / 2))) {

						// stops their growth and grows a new bubble
						if (growing.get(i))
							growing.put(i, false);
						if (growing.get(j))
							growing.put(j, false);
						newBubble(n);
					}
				}
			}
		}

		// grows the bubbles 
		for (int l = 0; l < n; l++) {
			if (growing.get(l)) {
				rlis.put(l, rlis.get(l) + 2);
			}
			// deletes bubbles that did not grow (their orgin is inside another bubble)
			if (rlis.get(l) == 1) {
				newBubble(l);
			}
		}

		// draws the bubbles
		for (int i = 0; i < n; i++) {
			g.setColor(color.get(i));
			g.fillOval((int) (plist.get(i).x - (rlis.get(i) / 2)), (int) (plist.get(i).y - (rlis.get(i) / 2)),
					rlis.get(i), rlis.get(i));

		}
		
		// Stops loop when all bubbles are done growing
		if (!growing.containsValue(true)) {
			grow = false;
			System.out.println("done");
		}

	}

	// Generates a new bubble
	public void newBubble(int index) {
		// max number of bubbles
		if (n < 10000) {
			int x, y;
			
			// Ensures that the new bubble is not already existing
			do { 
				x = rand.nextInt(wid - 1) + 1;
				y = rand.nextInt(hig - 1) + 1;
			} while (plist.containsValue(new Point(x, y)));

			// Initializes the bubble
			plist.put(index, new Point(x, y));
			rlis.put(index, 1);
			growing.put(index, true);
			color.put(index, getColor(x, y));
			if (index == n)
				n++;
		}
	}

	// get the color for the bubble (at the orgin) from the image
	public Color getColor(int x, int y) {
		int clr = image.getRGB(x, y);
		return new Color(clr);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// timed repaint
		repaint();
	}
}
