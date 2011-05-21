package endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	private AuctionSniperDriver driver;
	
	public void startBiddingIn(FakeAuctionServer auction) throws Exception {
		Main.main();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(MainWindow.STATUS_JOINING);
	}
	
	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}