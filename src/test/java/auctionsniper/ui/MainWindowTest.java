package auctionsniper.ui;

import static org.hamcrest.Matchers.equalTo;

import org.junit.After;
import org.junit.Test;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import endtoend.AuctionSniperDriver;

public class MainWindowTest {
	private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
	
	@Test 
	public void makesUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<String> buttonProbe =
			new ValueMatcherProbe<String>(equalTo("an item-id"), "join request");
		
		mainWindow.addUserRequestListener(new UserRequestListener() {
			public void joinAuction(String itemId) {
				buttonProbe.setReceivedValue(itemId);
			}
		});
		
		driver.typeItemIdAndClickJoinAuctionButton("an item-id");
		driver.check(buttonProbe);
	}
	
	@After
	public void close() {
		driver.dispose();
	}
}
