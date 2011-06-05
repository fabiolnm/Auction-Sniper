package auctionsniper.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import auctionsniper.SniperSnapshot;

public class MainWindow extends JFrame {
	public static final String NAME = "Auction Sniper Main";
	public static final String TITLE = "Auction Sniper";
	
	public static final String SNIPER_TABLE_NAME = "sniper_table";
	
	public static final String STATUS_JOINING = "Joining";
	public static final String STATUS_LOST = "Lost";
	public static final String STATUS_BIDDING = "Bidding";
	public static final String STATUS_WINNING = "Winning";
	public static final String STATUS_WON = "Won";
	
	public static final String[] STATUS_TEXT = {
		STATUS_JOINING, STATUS_BIDDING, STATUS_WINNING, STATUS_LOST, STATUS_WON
	};
	
	private final JTable sniperTable = createSniperTable();
	
	public MainWindow() {
		super(TITLE);
		setName(NAME);
		add(new JScrollPane(sniperTable));
		pack(); // fit to prefered size
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JTable createSniperTable() {
		JTable table = new JTable(1,4);
		table.setName(SNIPER_TABLE_NAME);
		return table;
	}

	public void showState(final SniperSnapshot state) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				sniperTable.setValueAt(state.itemId, 0, 0);
				sniperTable.setValueAt(state.lastPrice, 0, 1);
				sniperTable.setValueAt(state.lastBid, 0, 2);
				sniperTable.setValueAt(STATUS_TEXT[state.status.ordinal()], 0, 3);
			}
		});
	}
}