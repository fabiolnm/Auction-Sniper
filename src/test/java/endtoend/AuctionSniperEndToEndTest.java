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
		application.startBiddingIn(auction1);
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction1.announceClosed();
		application.showsSniperHasLostAuction(auction1, 0, 0);
	}

	@Test
	public void sniperJoinsMultipleAuctionsUntilAuctionCloses() throws Exception {
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
	public void multipleSnipersMakesAHigherBidButLoses() throws Exception {
		auction1.startSellingItem();
		auction2.startSellingItem();
		application.startBiddingIn(auction1, auction2);
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1000, 98, "other bidder");
		auction2.reportPrice(900, 53, "a third bidder");
		
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		application.hasShownSniperIsBidding(auction2, 900, 953);
		
		auction1.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedBidFrom(953, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.announceClosed();
		auction2.announceClosed();
		
		application.showsSniperHasLostAuction(auction1, 1000, 1098);
		application.showsSniperHasLostAuction(auction2, 900, 953);
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

	@Test 
	public void	multipleSnipersWinsAuctionsByBiddingHigher() throws Exception {
		auction1.startSellingItem();
		auction2.startSellingItem();
		application.startBiddingIn(auction1, auction2);
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1000, 98, "other bidder");
		auction2.reportPrice(1200, 50, "a third bidder");
		
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		application.hasShownSniperIsBidding(auction2, 1200, 1250);
		
		auction1.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedBidFrom(1250, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		auction2.reportPrice(1250, 32, ApplicationRunner.SNIPER_XMPP_ID);
		
		application.hasShownSniperIsWinning(auction1, 1098);
		application.hasShownSniperIsWinning(auction2, 1250);
		
		auction1.announceClosed();
		auction2.announceClosed();
		
		application.showsSniperHasWonAuction(auction1, 1098);
		application.showsSniperHasWonAuction(auction2, 1250);
	}

	@Test 
	public void	multipleSnipersLosesOneAuctionAndWinAnother() throws Exception {
		auction1.startSellingItem();
		auction2.startSellingItem();
		application.startBiddingIn(auction1, auction2);
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(1000, 98, "other bidder");
		auction2.reportPrice(1200, 50, "a third bidder");
		
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		application.hasShownSniperIsBidding(auction2, 1200, 1250);

		auction1.announceClosed();
		auction2.hasReceivedBidFrom(1250, ApplicationRunner.SNIPER_XMPP_ID);
		auction2.reportPrice(1250, 32, ApplicationRunner.SNIPER_XMPP_ID);

		application.showsSniperHasLostAuction(auction1, 1000, 1098);
		application.hasShownSniperIsWinning(auction2, 1250);
		
		auction2.announceClosed();
		
		application.showsSniperHasLostAuction(auction1, 1000, 1098);
		application.showsSniperHasWonAuction(auction2, 1250);
	}

	// Additional cleanup
	@After
	public void stopAuction() {
		auction1.stop();
		auction2.stop();
	}
	
	@After 
	public void stopApplication() {
		application.stop();
	}
}