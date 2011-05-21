package endtoend;

import static com.objogate.wl.swing.driver.ComponentDriver.named;
import static com.objogate.wl.swing.driver.ComponentDriver.showingOnScreen;
import static com.objogate.wl.swing.driver.JFrameDriver.topLevelFrame;
import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class ApplicationRunner {
	private JFrameDriver driver;
	
	public void startBidding() throws Exception {
		Main.main();
		
		int timeout = 1000, pollDelay = 100;
		
		// A Gesture interacts with the user interface through the input devices. 
		// Window Licker runs tests by performing Gestures and using Probes to observe the results.
		driver = new JFrameDriver(new GesturePerformer(), 
			// topLevelFrame: a ComponentFinder that expects to find a single GUI component.
			// named and showingOnScreen are WindowLicker matchers, 
			// to describe user interface components that are searched for by ComponentFinders
			topLevelFrame(named(MainWindow.NAME), showingOnScreen()),
			
			// A Probe samples the system and reports whether the last sample satisfies some test criteria. 
			// Probes are run asynchronously by a Prober, which is responsible for inserting the probe into 
			// the system under test, synchronising with its execution and reporting the result of the probe.
			new AWTEventQueueProber(timeout, pollDelay));
		
		driver.hasTitle(MainWindow.TITLE);
	}
	
	public void stop() {
		if (driver!=null)
			driver.dispose();
	}
}