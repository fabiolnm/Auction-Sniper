package auctionsniper.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import auctionsniper.SniperPortfolio;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperStatus;
import auctionsniper.util.Defect;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperPortfolio.Listener {
	private static final String[] STATUS_TEXT = {
		MainWindow.STATUS_JOINING, 
		MainWindow.STATUS_BIDDING, 
		MainWindow.STATUS_WINNING, 
		MainWindow.STATUS_LOST, 
		MainWindow.STATUS_WON
	};
	
	private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
	private LinkedHashMap<String, SniperSnapshot> 
		snapshotsByItemId = new LinkedHashMap<String, SniperSnapshot>();
	
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
		int rowIndex = rowMatching(snapshot);
		snapshots.set(rowIndex, snapshot);
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	private int rowMatching(SniperSnapshot snapshot) {
		String itemId = snapshot.itemId;
		
		SniperSnapshot olderSnapshot = snapshotsByItemId.put(itemId, snapshot);
		if (olderSnapshot == null)
			throw new Defect("No existing sniper for " + itemId);
		
		return snapshots.indexOf(olderSnapshot);
	}

	public void sniperAdded(AuctionSniper sniper) {
		sniper.setSniperListener(new SwingThreadSniperListener(this));
		addSniperSnapshot(sniper.getSnapshot());
	}

	private void addSniperSnapshot(SniperSnapshot snapshot) {
		snapshots.add(snapshot);
		snapshotsByItemId.put(snapshot.itemId, snapshot);
		int rowToUpdate = snapshots.indexOf(snapshot);
		fireTableRowsInserted(rowToUpdate, rowToUpdate);
	}

	public void sniperStateChanged(SniperSnapshot snapshot) {
		updateSnapshot(snapshot);
	}

	public static String textFor(SniperStatus status) {
		return STATUS_TEXT[status.ordinal()];
	}
	
	class SwingThreadSniperListener implements SniperListener {
		private final SnipersTableModel delegate;

		public SwingThreadSniperListener(SnipersTableModel snipers) {
			delegate = snipers;
		}
		
		public void sniperStateChanged(final SniperSnapshot snapshot) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() { 
					delegate.sniperStateChanged(snapshot);
				}
			});
		}
	}
}