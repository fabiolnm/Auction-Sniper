package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final Auction auction;
	private final SniperListener sniperListener;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		switch (priceSource) {
		case FromSniper:
			sniperListener.sniperWinning();
			break;
		case FromOtherBidder:
			auction.bid(currentPrice + increment);
			sniperListener.sniperBidding();
			break;
		}
	}
}