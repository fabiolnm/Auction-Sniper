package auctionsniper;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String... args) throws Exception {
		Main main = new Main();
	}
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				JFrame ui = new JFrame();
				ui.setVisible(true);
				ui.setSize(300, 200);
				ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}