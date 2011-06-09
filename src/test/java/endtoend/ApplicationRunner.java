package endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = 
		String.format("%s@%s/%s", SNIPER_ID, FakeAuctionServer.XMPP_HOSTNAME, Main.AUCTION_RESOURCE);
	
	private AuctionSniperDriver driver;
	
	public void startBiddingIn(FakeAuctionServer... auctions) throws Exception {
		Main.main(arguments(auctions));
		
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.TITLE);
		driver.hasColumnTitles();
		
		for (FakeAuctionServer a : auctions)
			driver.showsSniperStatus(a.itemId, 0, 0, MainWindow.STATUS_JOINING);
	}

	private String[] arguments(FakeAuctionServer[] auctions) {
		int n = auctions.length;
		String[] params = new String[3 + n];
		params[0] = FakeAuctionServer.XMPP_HOSTNAME;
		params[1] = SNIPER_ID;
		params[2] = SNIPER_PASSWORD;
		
		for (int i = 0; i < n; i++)
			params[i+3] = auctions[i].itemId;
		return params;
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