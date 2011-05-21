package auctionsniper;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String MAIN_WINDOW_TITLE = "Auction Sniper";
	
	public static void main(String... args) throws Exception {
		Main main = new Main();
	}
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				JFrame ui = new JFrame(MAIN_WINDOW_TITLE);
				ui.setName(MAIN_WINDOW_NAME);
				ui.setVisible(true);
				ui.setSize(300, 200);
				ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}