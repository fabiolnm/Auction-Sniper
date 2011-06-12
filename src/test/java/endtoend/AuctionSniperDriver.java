package endtoend;

import static org.hamcrest.Matchers.equalTo;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.swing.matcher.IterableComponentsMatcher;
import com.objogate.wl.swing.matcher.JLabelTextMatcher;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {
	public AuctionSniperDriver(int timeout) {
		// A Gesture interacts with the user interface through the input devices. 
		// Window Licker runs tests by performing Gestures and using Probes to observe the results.
		super(new GesturePerformer(), 
			// topLevelFrame: a ComponentFinder that expects to find a single GUI component.
			// named and showingOnScreen are WindowLicker matchers, 
			// to describe user interface components that are searched for by ComponentFinders
			topLevelFrame(named(MainWindow.NAME), showingOnScreen()),
			
			// A Probe samples the system and reports whether the last sample satisfies some test criteria. 
			// Probes are run asynchronously by a Prober, which is responsible for inserting the probe into 
			// the system under test, synchronising with its execution and reporting the result of the probe.
			new AWTEventQueueProber(timeout, 100));

		hasTitle(MainWindow.TITLE);
	}

	public void hasColumnTitles() {
		new JTableHeaderDriver(this, JTableHeader.class)
			.hasHeaders(IterableComponentsMatcher.matching(
				JLabelTextMatcher.withLabelText("Item"), 
				JLabelTextMatcher.withLabelText("Last Price"),
				JLabelTextMatcher.withLabelText("Last Bid"), 
				JLabelTextMatcher.withLabelText("State")));
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
		new JTableDriver(this, named(MainWindow.SNIPER_TABLE_NAME))
			.hasRow(IterableComponentsMatcher.matching(
				JLabelTextMatcher.withLabelText(equalTo(itemId)),
				JLabelTextMatcher.withLabelText(equalTo(String.valueOf(lastPrice))),
				JLabelTextMatcher.withLabelText(equalTo(String.valueOf(lastBid))),
				JLabelTextMatcher.withLabelText(equalTo(status))));
	}

	public void typeItemIdAndClickJoinAuctionButton(String itemId) {
		itemIdField().replaceAllText(itemId);
		bidButton().click();
	}
	
	private JTextFieldDriver itemIdField() {
		JTextFieldDriver newItemId = new JTextFieldDriver(this, 
				JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
		newItemId.focusWithMouse();
		return newItemId;
	}
	
	private JButtonDriver bidButton() {
		return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
	}
}