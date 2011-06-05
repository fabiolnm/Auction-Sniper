package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final Auction auction;
	private final SniperListener sniperListener;
	private boolean isWinning;
	
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	public void auctionClosed() {
		if (isWinning)
			sniperListener.sniperWon();
		else sniperListener.sniperLost();
	}

	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		isWinning = priceSource == PriceSource.FromSniper;
		if (isWinning)
			snapshot = snapshot.winning(currentPrice);
		else {
			int bid = currentPrice + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(currentPrice, bid);
		}
		sniperListener.sniperStateChanged(snapshot);
	}
}