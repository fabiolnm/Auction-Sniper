package auctionsniper;

public class SniperLauncher implements UserRequestListener {
	private final AuctionHouse auctionHouse;
	private final SniperCollector collector;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	public void joinAuction(Item item) {
		Auction auction = auctionHouse.auctionFor(item.id);
		AuctionSniper sniper = new AuctionSniper(item, auction);
		auction.setAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}
}
