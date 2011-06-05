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
		application.showsSniperHasLostAuction(auction, 0, 0); // Step 5
	}
	
	@Test 
	public void sniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098);
		
		auction.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(auction, 1000, 1098);
	}
	
	@Test 
	public void	sniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction, 1000, 1098);
		
		auction.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction, 1098);
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(auction, 1098);
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