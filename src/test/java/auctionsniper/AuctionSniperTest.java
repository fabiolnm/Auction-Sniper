package auctionsniper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import auctionsniper.AuctionEventListener.PriceSource;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final String ITEM_ID = "item-id";
	private final Integer STOP_PRICE = 2000;
	private final Mockery context = new Mockery();

	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(new Item(ITEM_ID, STOP_PRICE), auction);

	private final States sniperState = context.states("sniper");
	
	@Before
	public void setupSniperListener() {
		sniper.setSniperListener(sniperListener);
	}
	
	@Test
	public void reportsLostWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {{
			one(sniperListener).sniperStateChanged(SniperSnapshot.joining(ITEM_ID).closed());
		}});
		sniper.auctionClosed();
	}
	
	@Test 
	public void	reportsLostIfAuctionClosesWhenBidding() {
		final int price = 123, increment = 45, bid = price + increment; 
		context.checking(new Expectations() {{
			ignoring(auction);
			
			SniperSnapshot state = SniperSnapshot.joining(ITEM_ID).bidding(price, bid);
			allowing(sniperListener).sniperStateChanged(state);
			then(sniperState.is("bidding"));
			
			atLeast(1).of(sniperListener).sniperStateChanged(state.closed());
			when(sniperState.is("bidding"));
		}});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrivesFromOtherBidder() {
		final SniperSnapshot state = SniperSnapshot.joining(ITEM_ID);
		final int price = 1001, increment = 25, bid = price + increment;
		context.checking(new Expectations() {{
			one(auction).bid(price + increment);
			atLeast(1).of(sniperListener).sniperStateChanged(state.bidding(price, bid));
		}});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test 
	public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
		final SniperSnapshot state = SniperSnapshot.joining(ITEM_ID);
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperStateChanged(state.winning(123));
		}});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
	}
	
	@Test 
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {{
			ignoring(auction);
			
			final SniperSnapshot state = SniperSnapshot.joining(ITEM_ID).winning(123);
			allowing(sniperListener).sniperStateChanged(state.winning(123)); 
			then(sniperState.is("winning"));
			
			atLeast(1).of(sniperListener).sniperStateChanged(state.closed()); 
			when(sniperState.is("winning"));
		}});
		
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	@Test
	public void doesNotBidAndReportIsLosingIfFirstPriceIsHigherThanStopPrice() {
		final int increment = 1, lastPrice = STOP_PRICE;
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperStateChanged(SniperSnapshot.joining(ITEM_ID).losing(lastPrice));
		}});
		sniper.currentPrice(lastPrice, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void doesNotBidAndReportIsLosingWhenPriceIsHigherThanStopPrice() {
		final int initialPrice = 234, initialIncrement = 30, lastBid = initialPrice + initialIncrement;
		final int increment = 1, lastPrice = STOP_PRICE;
		context.checking(new Expectations() {{
			allowing(auction).bid(lastBid);
			
			SniperSnapshot biddingSnapshot = SniperSnapshot.joining(ITEM_ID).bidding(initialPrice, lastBid);
			allowing(sniperListener).sniperStateChanged(with(biddingSnapshot));
			then(sniperState.is("bidding"));
			
			SniperSnapshot losingSnapshot = biddingSnapshot.losing(lastPrice);
			atLeast(1).of(sniperListener).sniperStateChanged(with(losingSnapshot));
			when(sniperState.is("bidding"));
		}});
		sniper.currentPrice(initialPrice, initialIncrement, PriceSource.FromOtherBidder);
		sniper.currentPrice(lastPrice, increment, PriceSource.FromOtherBidder);
	}
}