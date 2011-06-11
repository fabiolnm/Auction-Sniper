package auctionsniper.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperStatus;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
	private static final String[] STATUS_TEXT = {
		MainWindow.STATUS_JOINING, 
		MainWindow.STATUS_BIDDING, 
		MainWindow.STATUS_WINNING, 
		MainWindow.STATUS_LOST, 
		MainWindow.STATUS_WON
	};
	
	private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
	
	public int getRowCount() {
		return snapshots.size();
	}

	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).header();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
	}
	
	public void updateSnapshot(SniperSnapshot snapshot) {
		fireTableRowsUpdated(0, 0);
	}

	public void addSniper(SniperSnapshot snapshot) {
		snapshots.add(snapshot);
		int rowToUpdate = snapshots.indexOf(snapshot);
		fireTableRowsInserted(rowToUpdate, rowToUpdate);
	}

	public void sniperStateChanged(SniperSnapshot snapshot) {
		updateSnapshot(snapshot);
	}

	public static String textFor(SniperStatus status) {
		return STATUS_TEXT[status.ordinal()];
	}
}