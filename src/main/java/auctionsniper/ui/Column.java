package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

public enum Column {
	ITEM_IDENTIFIER {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.itemId;
		}
	},
	LAST_PRICE {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastPrice;
		}
	},
	LAST_BID {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return snapshot.lastBid;
		}
	},
	SNIPER_STATE {
		@Override
		public Object valueIn(SniperSnapshot snapshot) {
			return SnipersTableModel.textFor(snapshot.status);
		}
	};
	
	abstract public Object valueIn(SniperSnapshot snapshot);

	public static Column at(int columnIndex) {
		if (columnIndex >= values().length)
			throw new IllegalArgumentException(String.format("Invalid column index [%s]", columnIndex));
		return values()[columnIndex];
	}
}