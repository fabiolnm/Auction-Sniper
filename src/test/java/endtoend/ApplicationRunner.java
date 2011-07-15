package endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;
import auctionsniper.xmpp.XmppAuctionHouse;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = 
		String.format("%s@%s/%s", SNIPER_ID, FakeAuctionServer.XMPP_HOSTNAME, XmppAuctionHouse.AUCTION_RESOURCE);
	
	private AuctionSniperDriver driver;
	
	public void startBiddingIn(FakeAuctionServer... auctions) throws Exception {
		startSniper();
		
		for (FakeAuctionServer a : auctions) {
			driver.typeItemId(a.itemId);
			driver.clickJoinAuctionButton();
			driver.showsSniperStatus(a.itemId, 0, 0, MainWindow.STATUS_JOINING);
		}
	}

	public void startBiddingWithStopPrice(FakeAuctionServer auction, int stopPrice) throws Exception {
		startSniper();
		driver.typeItemId(auction.itemId);
		driver.typeStopPrice(stopPrice);
		driver.clickJoinAuctionButton();
		driver.showsSniperStatus(auction.itemId, 0, 0, MainWindow.STATUS_JOINING);
	}

	private void startSniper() throws Exception {
		Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.TITLE);
		driver.hasColumnTitles();
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, MainWindow.STATUS_LOST);
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
	}

	public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, MainWindow.STATUS_LOSING);
	}

	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}