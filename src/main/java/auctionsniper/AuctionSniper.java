package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final String itemId;
	private final Auction auction;
	private final SniperListener sniperListener;
	private boolean isWinning;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	public void auctionClosed() {
		if (isWinning)
			sniperListener.sniperWon();
		else sniperListener.sniperLost();
	}

	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		SniperSnapshot state;
		if (isWinning)
			state = new SniperSnapshot(itemId, currentPrice, currentPrice, SniperStatus.WINNING);
		else {
			int bid = currentPrice + increment;
			auction.bid(bid);
			state = new SniperSnapshot(itemId, currentPrice, bid, SniperStatus.BIDDING);
		}
		sniperListener.sniperStateChanged(state);
	}
}