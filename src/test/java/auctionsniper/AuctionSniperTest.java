package auctionsniper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import auctionsniper.AuctionEventListener.PriceSource;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final String ITEM_ID = "item-id";
	private final Mockery context = new Mockery();

	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

	private final States sniperState = context.states("sniper");
	
	@Test
	public void reportsLostWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {{
			one(sniperListener).sniperLost();
		}});
		sniper.auctionClosed();
	}
	
	@Test 
	public void	reportsLostIfAuctionClosesWhenBidding() {
		final int price = 123, increment = 45, bid = price + increment; 
		context.checking(new Expectations() {{
			ignoring(auction);
			
			allowing(sniperListener).sniperBidding(new SniperState(ITEM_ID, price, bid));
			then(sniperState.is("bidding"));
			
			atLeast(1).of(sniperListener).sniperLost();
			when(sniperState.is("bidding"));
		}});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrivesFromOtherBidder() {
		final int price = 1001, increment = 25, bid = price + increment;
		context.checking(new Expectations() {{
			one(auction).bid(price + increment);
			atLeast(1).of(sniperListener).sniperBidding(new SniperState(ITEM_ID, price, bid));
		}});
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test 
	public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperWinning(new SniperState(ITEM_ID, 123, 123));
		}});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
	}
	
	@Test 
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {{
			ignoring(auction);
			
			allowing(sniperListener).sniperWinning(new SniperState(ITEM_ID, 123, 123)); 
			then(sniperState.is("winning"));
			
			atLeast(1).of(sniperListener).sniperWon(); 
			when(sniperState.is("winning"));
		}});
		
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}	
}