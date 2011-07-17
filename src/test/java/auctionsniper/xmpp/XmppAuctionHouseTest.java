package auctionsniper.xmpp;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import endtoend.ApplicationRunner;
import endtoend.FakeAuctionServer;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;

public class XmppAuctionHouseTest {
	private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
	private XmppAuctionHouse auctionHouse;

	@Before
	public void startServerAndAuctionHouse() throws Exception {
		auctionServer.startSellingItem();
		auctionHouse = 
			XmppAuctionHouse.connectTo(FakeAuctionServer.XMPP_HOSTNAME, 
				ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);
	}

	@After
	public void releaseResources() {
		auctionServer.stop();
		auctionHouse.disconnect();
	}
	
	@Test 
	public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
		Auction auction = auctionHouse.auctionFor(auctionServer.itemId);
		
		AuctionClosesWhenNotifiedByXmppAuctionHouseEventListener listener = 
			new AuctionClosesWhenNotifiedByXmppAuctionHouseEventListener();
		auction.setAuctionEventListener(listener);
		
		auction.join(); 
		auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); 
		auctionServer.announceClosed();
		listener.shouldHaveBeenNotifiedAuctionClosed();
	}
	
	private static class AuctionClosesWhenNotifiedByXmppAuctionHouseEventListener implements AuctionEventListener {
		// sincronização de threads
		// see http://download.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/CountDownLatch.html
		private CountDownLatch auctionWasClosed = new CountDownLatch(1); 

		public void auctionClosed() { 
			auctionWasClosed.countDown(); 
		}
		
		public void currentPrice(int price, int increment, PriceSource priceSource) {
		}

		public void auctionFailed() {
		}
		
		public void shouldHaveBeenNotifiedAuctionClosed() throws Exception {
			assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
		}
	}
}
