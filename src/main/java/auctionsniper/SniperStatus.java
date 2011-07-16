package auctionsniper;

import auctionsniper.util.Defect;

public enum SniperStatus {
	JOINING("Joining") {
		@Override
		public SniperStatus whenAuctionClosed() {
			return LOST;
		}
	},
	BIDDING("Bidding") {
		@Override
		public SniperStatus whenAuctionClosed() {
			return LOST;
		}
	},
	LOSING("Losing") {
		@Override
		public SniperStatus whenAuctionClosed() {
			return LOST;
		}
	}, 
	WINNING("Winning") {
		@Override
		public SniperStatus whenAuctionClosed() {
			return WON;
		}
	},
	LOST("Lost"), WON("Won");

	public final String text;
	
	private SniperStatus(String text) {
		this.text = text;
	}

	public SniperStatus whenAuctionClosed() {
		throw new Defect("Auction is already closed");
	}
}