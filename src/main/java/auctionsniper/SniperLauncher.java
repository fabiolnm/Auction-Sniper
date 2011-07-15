package auctionsniper;

import auctionsniper.xmpp.XmppAuctionHouse;

public class SniperLauncher implements UserRequestListener {
	private final XmppAuctionHouse auctionHouse;
	private final SniperCollector collector;

	public SniperLauncher(XmppAuctionHouse auctionHouse, SniperCollector collector) {
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	public void joinAuction(String itemId) {
		Auction auction = auctionHouse.auctionFor(itemId);
		AuctionSniper sniper = new AuctionSniper(itemId, auction);
		auction.setAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}
}
