package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final SniperListener sniperListener;

	public AuctionSniper(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}

	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	public void currentPrice(int currentPrice, int increment) {
		// TODO Auto-generated method stub
	}
}