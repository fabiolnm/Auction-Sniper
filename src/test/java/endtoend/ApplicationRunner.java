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
		
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(MainWindow.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(MainWindow.STATUS_LOST);
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(MainWindow.STATUS_WINNING);
	}

	public void showsSniperHasWonAuction() {
		driver.showsSniperStatus(MainWindow.STATUS_WON);
	}

	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}