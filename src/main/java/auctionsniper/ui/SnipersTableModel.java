package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperStatus;

class SnipersTableModel extends AbstractTableModel {
	private static final String[] STATUS_TEXT = {
		MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING, MainWindow.STATUS_LOST, MainWindow.STATUS_WON
	};
	
	private SniperSnapshot snapshot;
	
	public int getRowCount() {
		return 1;
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(snapshot);
	}
	
	public void updateSnapshot(SniperSnapshot snapshot) {
		this.snapshot = snapshot;
		fireTableRowsUpdated(0, 0);
	}

	public static String textFor(SniperStatus status) {
		return STATUS_TEXT[status.ordinal()];
	}
}