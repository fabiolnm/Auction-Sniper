package auctionsniper.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	
	public MainWindow() {
		super(TITLE);
		setName(NAME);
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

	public void showState(final SniperSnapshot snapshot) {
		snipers.updateSnapshot(snapshot);
	}
	
	static class SnipersTableModel extends AbstractTableModel {
		private static final String[] STATUS_TEXT = {
			STATUS_JOINING, STATUS_BIDDING, STATUS_WINNING, STATUS_LOST, STATUS_WON
		};
		
		private SniperSnapshot snapshot;
		
		public int getRowCount() {
			return 1;
		}

		public int getColumnCount() {
			return 4;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0: return snapshot.itemId;
			case 1: return snapshot.lastPrice;
			case 2: return snapshot.lastBid;
			case 3: return STATUS_TEXT[snapshot.status.ordinal()];
			}
			throw new IllegalArgumentException(String.format("Invalid column index [%s]", columnIndex));
		}
		
		public void updateSnapshot(SniperSnapshot snapshot) {
			this.snapshot = snapshot;
			fireTableRowsUpdated(0, 0);
		}
	}
}