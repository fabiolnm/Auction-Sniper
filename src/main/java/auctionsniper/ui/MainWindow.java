package auctionsniper.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow extends JFrame {
	public static final String NAME = "Auction Sniper Main";
	public static final String TITLE = "Auction Sniper";
	
	public static final String SNIPER_STATUS_NAME = "sniper_status";
	public static final String STATUS_JOINING = "Joining";
	public static final String STATUS_LOST = "Lost";
	public static final String STATUS_BIDDING = "Bidding";
	public static final String STATUS_WINNING = "Winning";
	public static final String STATUS_WON = "Won";
	
	private final JLabel sniperStatus = createLabel(STATUS_JOINING);
	
	public MainWindow() {
		super(TITLE);
		setName(NAME);
		add(sniperStatus);
		pack(); // fit to prefered size
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JLabel createLabel(String initialText) {
		JLabel result = new JLabel(initialText);
		result.setName(SNIPER_STATUS_NAME);
		return result;
	}

	public void showStatusLost() {
		sniperStatus.setText(STATUS_LOST);
	}

	public void showStatusBidding() {
		sniperStatus.setText(STATUS_BIDDING);
	}
}