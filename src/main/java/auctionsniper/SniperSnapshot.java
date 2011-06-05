package auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SniperSnapshot {
	public final String itemId;
	public final int lastPrice, lastBid;
	public final SniperStatus status;
	
	public SniperSnapshot(String itemId) {
		this(itemId, 0, 0, SniperStatus.JOINING);
	}
	
	public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperStatus status) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.status = status;
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