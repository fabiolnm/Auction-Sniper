package auctionsniper;

public interface SniperListener {
	void sniperLost();

	void sniperBidding(SniperState state);

	void sniperWinning();

	void sniperWon();
}