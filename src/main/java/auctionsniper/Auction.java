package auctionsniper;

public interface Auction {
	void join();

	void bid(int amount);

	void setAuctionEventListener(AuctionEventListener listener);
}
