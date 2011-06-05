package auctionsniper;

public class SniperState {
	public final String itemId;
	public final int lastPrice, lastBid;
	
	public SniperState(String itemId, int lastPrice, int lastBid) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
	}
}