package endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	
	private AuctionSniperDriver driver;
	
	public void startBiddingIn(FakeAuctionServer auction) throws Exception {
		Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.itemId);
		
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(MainWindow.STATUS_JOINING);
	}
	
	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}