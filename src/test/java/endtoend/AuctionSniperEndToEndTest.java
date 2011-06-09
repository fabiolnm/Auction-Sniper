package endtoend;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {
	private final FakeAuctionServer auction1 = new FakeAuctionServer("item-54321");
	private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction1.startSellingItem();
		auction2.startSellingItem();
		
		application.startBiddingIn(auction1, auction2);
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

		auction1.announceClosed();
		auction2.announceClosed();
		
		application.showsSniperHasLostAuction(auction1, 0, 0);
		application.showsSniperHasLostAuction(auction2, 0, 0);
	}
	
	@Test 
	public void sniperMakesAHigherBidButLoses() throws Exception {
		auction1.startSellingItem();
		application.startBiddingIn(auction1);
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		
		auction1.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.announceClosed();
		application.showsSniperHasLostAuction(auction1, 1000, 1098);
	}
	
	@Test 
	public void	sniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction1.startSellingItem();
		application.startBiddingIn(auction1);
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		
		auction1.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction1.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(auction1, 1098);
		
		auction1.announceClosed();
		application.showsSniperHasWonAuction(auction1, 1098);
	}
	
	// Additional cleanup
	@After
	public void stopAuction() {
		auction1.stop();
	}
	
	@After 
	public void stopApplication() {
		application.stop();
	}
}