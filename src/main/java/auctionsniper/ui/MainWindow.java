package auctionsniper.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
	
	private final SnipersTableModel snipers;
	
	public MainWindow(SnipersTableModel snipers) {
		super(TITLE);
		setName(NAME);
		this.snipers = snipers;
		add(new JScrollPane(createSniperTable()));
		pack(); // fit to prefered size
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JTable createSniperTable() {
		JTable table = new JTable(snipers);
		table.setName(SNIPER_TABLE_NAME);
		return table;
	}
}