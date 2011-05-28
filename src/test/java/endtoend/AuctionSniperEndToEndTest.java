package endtoend;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {
	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem(); // Step 1
		application.startBiddingIn(auction); // Step 2
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); // Step 3
		auction.announceClosed(); // Step 4
		application.showsSniperHasLostAuction(); // Step 5
	}
	
	@Test 
	public void sniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();
		
		auction.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@Test 
	public void	sniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();
		
		auction.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning();
		
		auction.announceClosed();
		application.showsSniperHasWonAuction();
	}
	
	// Additional cleanup
	@After
	public void stopAuction() {
		auction.stop();
	}
	
	@After 
	public void stopApplication() {
		application.stop();
	}
}