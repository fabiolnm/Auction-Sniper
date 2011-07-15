package auctionsniper;

import java.util.ArrayList;

public class SniperPortfolio implements SniperCollector {
	public interface Listener {
		void sniperAdded(AuctionSniper sniper);
	}
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	private ArrayList<AuctionSniper> snipers = new ArrayList<AuctionSniper>();
	
	public void addSniper(AuctionSniper sniper) {
		snipers.add(sniper);
		for (Listener l : listeners)
			l.sniperAdded(sniper);
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}
}
