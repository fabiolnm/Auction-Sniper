package endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = 
		String.format("%s@%s/%s", SNIPER_ID, FakeAuctionServer.XMPP_HOSTNAME, Main.AUCTION_RESOURCE);
	
	private AuctionSniperDriver driver;
	
	public void startBiddingIn(FakeAuctionServer auction) throws Exception {
		Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId);
		
		driver = new AuctionSniperDriver(10000);
		driver.showsSniperStatus(auction.itemId, 0, 0, MainWindow.STATUS_JOINING);
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

	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}