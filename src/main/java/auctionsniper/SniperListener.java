package auctionsniper;

public interface SniperListener {
	void sniperLost();

	void sniperBidding(SniperState state);

	void sniperWinning(SniperState state);

	void sniperWon();
}