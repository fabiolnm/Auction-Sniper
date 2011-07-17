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

	@Test
	public void sniperLosesAnAuctionWhenPriceIsTooHigh() throws Exception {
		auction1.startSellingItem();
		application.startBiddingWithStopPrice(auction1, 1100);
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auction1.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(auction1, 1000, 1098);
		auction1.hasReceivedBidFrom(1098, ApplicationRunner.SNIPER_XMPP_ID);

		auction1.reportPrice(1197, 10, "third party");
		application.hasShownSniperIsLosing(auction1, 1197, 1098);

		auction1.reportPrice(1207, 10, "fourth party");
		application.hasShownSniperIsLosing(auction1, 1207, 1098);

		auction1.announceClosed();
		application.showsSniperHasLostAuction(auction1, 1207, 1098);
	}
	
	@Test
	public void sniperReportsInvalidAuctionMessageAndStopsRespondingToEvents() throws Exception {
		String brokenMessage = "a broken message"; 
		auction1.startSellingItem(); 
		auction2.startSellingItem();
		application.startBiddingIn(auction1, auction2); 
		
		auction1.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction1.reportPrice(500, 20, "other bidder"); 
		auction1.hasReceivedBidFrom(520, ApplicationRunner.SNIPER_XMPP_ID);
		
		// AuctionMessageTranslator$AuctionEvent will throw a ArrayIndexOutOfBoundsException
		// when AuctionEvent.from("a broken message") tries to extract the fields from the event string
		auction1.sendInvalidMessageContaining(brokenMessage); 
		application.showsSniperHasFailed(auction1);
		
		auction1.reportPrice(520, 21, "other bidder");
		waitForAnotherAuctionEventToAssureSniperIgnoresFailedAuctionSubsequentEvents(); 
		
		application.reportsInvalidMessage(auction1, brokenMessage);
		application.showsSniperHasFailed(auction1);
	}

	// see chapter 27, Runaway Tests section 
	private void waitForAnotherAuctionEventToAssureSniperIgnoresFailedAuctionSubsequentEvents() throws Exception {
		auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); 
		auction2.reportPrice(600, 6, "other bidder"); 
		application.hasShownSniperIsBidding(auction2, 600, 606);	
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