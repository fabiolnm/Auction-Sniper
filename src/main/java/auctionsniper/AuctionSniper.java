package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
	private final Item item;
	private final Auction auction;
	private SniperSnapshot snapshot;
	private SniperListener sniperListener;

	public AuctionSniper(Item item, Auction auction) {
		this.item = item;
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(item.id);
	}

	public void auctionClosed() {
		notifyChange(snapshot.closed());
	}

	public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
		switch(priceSource) {
		case FromSniper:
			notifyChange(snapshot.winning(currentPrice));
			break;
		case FromOtherBidder:
			int bid = currentPrice + increment;
			if (item.allowsBid(bid)) {
				auction.bid(bid);
				notifyChange(snapshot.bidding(currentPrice, bid));
			} else notifyChange(snapshot.losing(currentPrice));
		}
	}

	public void auctionFailed() {
		// TODO Auto-generated method stub
		
	}

	private void notifyChange(SniperSnapshot snapshot) {
		this.snapshot = snapshot;
		sniperListener.sniperStateChanged(snapshot);
	}
	
	public void setSniperListener(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}
	
	public SniperSnapshot getSnapshot() {
		return snapshot;
	}
}