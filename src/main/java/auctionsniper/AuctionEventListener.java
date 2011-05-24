package auctionsniper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {
	void auctionClosed();

	void currentPrice(int currentPrice, int increment);
}