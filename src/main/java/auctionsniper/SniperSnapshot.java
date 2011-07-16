package auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SniperSnapshot {
	public final String itemId;
	public final int lastPrice, lastBid;
	public final SniperStatus status;
	
	private SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperStatus status) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.status = status;
	}

	public static SniperSnapshot joining(String itemId) {
		return new SniperSnapshot(itemId, 0, 0, SniperStatus.JOINING);
	}
	
	public SniperSnapshot bidding(int price, int bid) {
		return new SniperSnapshot(itemId, price, bid, SniperStatus.BIDDING);
	}

	public SniperSnapshot losing(int price) {
		return new SniperSnapshot(itemId, price, lastBid, SniperStatus.LOSING);
	}

	public SniperSnapshot winning(int price) {
		return new SniperSnapshot(itemId, price, price, SniperStatus.WINNING);
	}

	public SniperSnapshot closed() {
		return new SniperSnapshot(itemId, lastPrice, lastBid, status.whenAuctionClosed());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}