package auctionsniper;

import javax.swing.SwingUtilities;

import auctionsniper.ui.SnipersTableModel;
import auctionsniper.xmpp.XmppAuctionHouse;

public class SniperLauncher implements UserRequestListener {
	private final XmppAuctionHouse auctionHouse;
	private final SnipersTableModel snipers;

	public SniperLauncher(XmppAuctionHouse auctionHouse, SnipersTableModel snipers) {
		this.auctionHouse = auctionHouse;
		this.snipers = snipers;
	}

	public void joinAuction(String itemId) {
		Auction auction = auctionHouse.auctionFor(itemId);
		
		snipers.addSniper(SniperSnapshot.joining(itemId));
		AuctionSniper sniper = new AuctionSniper(itemId, auction, 
					new SwingThreadSniperListener(snipers));
		auction.setAuctionEventListener(sniper);
		auction.join();
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
