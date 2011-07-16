package endtoend;

import auctionsniper.Main;
import auctionsniper.SniperStatus;
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
			driver.showsSniperStatus(a.itemId, 0, 0, SniperStatus.JOINING.text);
		}
	}

	public void startBiddingWithStopPrice(FakeAuctionServer auction, int stopPrice) throws Exception {
		startSniper();
		driver.typeItemId(auction.itemId);
		driver.typeStopPrice(stopPrice);
		driver.clickJoinAuctionButton();
		driver.showsSniperStatus(auction.itemId, 0, 0, SniperStatus.JOINING.text);
	}

	private void startSniper() throws Exception {
		Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.TITLE);
		driver.hasColumnTitles();
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, SniperStatus.LOST.text);
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, SniperStatus.BIDDING.text);
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.itemId, winningBid, winningBid, SniperStatus.WINNING.text);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastPrice, SniperStatus.WON.text);
	}

	public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.itemId, lastPrice, lastBid, SniperStatus.LOSING.text);
	}

	public void showsSniperHasFailed(FakeAuctionServer auction) {
		driver.showsSniperStatus(auction.itemId, 0, 0, SniperStatus.FAILED.text);
	}

	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}