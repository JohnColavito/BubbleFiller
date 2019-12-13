package PP14;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Circles {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Circles");
		ScreenV2 screen = new ScreenV2();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(screen);
		screen.setPreferredSize(new Dimension(1280, 853));
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

}
