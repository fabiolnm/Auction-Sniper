package auctionsniper.ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
	public static final String NAME = "Auction Sniper Main";
	public static final String TITLE = "Auction Sniper";
	
	public static final String SNIPER_STATUS_NAME = "sniper_status";
	public static final String STATUS_JOINING = "Joining";
	
	public MainWindow() {
		super(TITLE);
		setName(NAME);
		setVisible(true);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
