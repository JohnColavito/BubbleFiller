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

	int wid = 1280, hig = 853;

	int n = 700;

	boolean grow = true;
	Map<Integer, Point> plist = new HashMap<Integer, Point>();
	Map<Integer, Integer> rlis = new HashMap<Integer, Integer>();
	Map<Integer, Boolean> growing = new HashMap<Integer, Boolean>();
	Map<Integer, Color> color = new HashMap<Integer, Color>();

	private Timer timer;
	private int delay = 50;

	File file = new File("src/PP14/christmas-elf-4679016_1280.jpg");
	private BufferedImage image;

	public ScreenV2() {
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer = new Timer(delay, this);
		timer.start();
		for (int i = 0; i < n; i++) {
			newBubble(i);
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);

//		if (!grow) {
//			for (int i = 0; i < n; i++) {
//				newBubble(i);
//			}
//		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (growing.get(i)) {
					if (plist.get(i).x + rlis.get(i) / 2 >= wid || plist.get(i).x - rlis.get(i) / 2 <= 0
							|| plist.get(i).y + rlis.get(i) / 2 >= hig || plist.get(i).y - rlis.get(i) / 2 <= 0) {
						growing.put(i, false);

						newBubble(n);
					}
				}

				if (i != j && (growing.get(i) || growing.get(j))) {
					double dx = Math.abs((plist.get(i).x) - (plist.get(j).x));
					double dy = Math.abs((plist.get(i).y) - (plist.get(j).y));

					double maxDistance2 = (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
					if (maxDistance2 < ((rlis.get(i) / 2) + (rlis.get(j) / 2))) {

						if (growing.get(i))
							growing.put(i, false);
						if (growing.get(j))
							growing.put(j, false);
						newBubble(n);
//						newBubble(n);
					}
				}
			}
		}

		if (!growing.containsValue(true)) {
			grow = false;
			System.out.println("done");
		}

		for (int l = 0; l < n; l++) {
			if (growing.get(l)) {
				rlis.put(l, rlis.get(l) + 2);
			}
			if (rlis.get(l) == 1) {
				newBubble(l);
			}
		}

		for (int i = 0; i < n; i++) {
			g.setColor(color.get(i));
			g.fillOval((int) (plist.get(i).x - (rlis.get(i) / 2)), (int) (plist.get(i).y - (rlis.get(i) / 2)),
					rlis.get(i), rlis.get(i));

		}

	}

	public void newBubble(int index) {
		if (n < 10000) {
			int x, y;
			do {
				x = rand.nextInt(wid - 1) + 1;
				y = rand.nextInt(hig - 1) + 1;
			} while (plist.containsValue(new Point(x, y)));

//			while (xlis.containsValue(x) && ylis.containsValue(y));

//			xlis.put(index, x);
//			ylis.put(index, y);

			plist.put(index, new Point(x, y));
			rlis.put(index, 1);
			growing.put(index, true);
			color.put(index, getColor(x, y));
			if (index == n)
				n++;
		}
	}

	public Color getColor(int x, int y) {
		// Getting pixel color by position x and y
		int clr = image.getRGB(x, y);
		return new Color(clr);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}