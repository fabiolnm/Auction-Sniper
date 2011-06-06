package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

public enum Column {
	ITEM_IDENTIFIER("Item") {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.itemId;
		}
	},
	LAST_PRICE("Last Price") {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastPrice;
		}
	},
	LAST_BID("Last Bid") {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastBid;
		}
	},
	SNIPER_STATE("State") {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return SnipersTableModel.textFor(snapshot.status);
		}
	};
	
	abstract public Object valueIn(SniperSnapshot snapshot);

	private final String header;

	private Column(String header) {
		this.header = header;
	}

	public String header() {
		return header;
	}
	
	public static Column at(int columnIndex) {
		if (columnIndex >= values().length)
			throw new IllegalArgumentException(String.format("Invalid column index [%s]", columnIndex));
		return values()[columnIndex];
	}
}