package auctionsniper.ui;

import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import auctionsniper.UserRequestListener;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import endtoend.AuctionSniperDriver;

public class MainWindowTest {
	private final SnipersTableModel tableModel = new SnipersTableModel();
	private final MainWindow mainWindow = new MainWindow(tableModel);
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
}
