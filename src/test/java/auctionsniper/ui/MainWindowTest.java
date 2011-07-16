package auctionsniper.ui;

import static org.hamcrest.Matchers.equalTo;

import org.junit.After;
import org.junit.Test;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import endtoend.AuctionSniperDriver;

public class MainWindowTest {
	private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
	
	@Test 
	public void makesUserRequestWhenJoinButtonClicked() {
		Item anItem = new Item("an item-id", 789);
		final ValueMatcherProbe<Item> itemProbe =
			new ValueMatcherProbe<Item>(equalTo(anItem), "join request");
		
		mainWindow.addUserRequestListener(new UserRequestListener() {
			public void joinAuction(Item item) {
				itemProbe.setReceivedValue(item);
			}
		});
		
		driver.typeItemId(anItem.id);
		driver.typeStopPrice(anItem.stopPrice);
		driver.clickJoinAuctionButton();
		driver.check(itemProbe);
	}
	
	@After
	public void close() {
		driver.dispose();
	}
}
