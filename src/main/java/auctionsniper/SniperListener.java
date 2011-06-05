package auctionsniper;

public interface SniperListener {
	void sniperLost();

	void sniperStateChanged(SniperSnapshot state);

	void sniperWon();
}