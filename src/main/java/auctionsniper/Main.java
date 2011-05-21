package auctionsniper;

import javax.swing.SwingUtilities;

import auctionsniper.ui.MainWindow;

public class Main {
	public static void main(String... args) throws Exception {
		Main main = new Main();
	}
	
	private MainWindow ui;
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow();
			}
		});
	}
}