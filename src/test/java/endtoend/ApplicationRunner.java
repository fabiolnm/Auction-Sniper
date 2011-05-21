package endtoend;

import auctionsniper.Main;

public class ApplicationRunner {
	private AuctionSniperDriver driver;
	
	public void startBidding() throws Exception {
		Main.main();
		driver = new AuctionSniperDriver(1000);
	}
	
	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}