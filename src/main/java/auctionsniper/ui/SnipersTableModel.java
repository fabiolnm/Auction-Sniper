package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperSnapshot;

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