package auctionsniper;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class SniperLauncherTest {
	private final Mockery context = new Mockery();
	private final States state = context.states("auction state").startsAs("not joined");
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private final SniperCollector collector = context.mock(SniperCollector.class);
	private final SniperLauncher launcher = new SniperLauncher(auctionHouse, collector);
	
	private final Auction auction = context.mock(Auction.class);
	
	@Test
	public void joinAuctionAfterSniperSetup() {
		final String itemId = "an item id";
		context.checking(new Expectations() {{
			allowing(auctionHouse).auctionFor(itemId); 
			will(returnValue(auction));
			
			oneOf(auction).setAuctionEventListener(with(aSniperForItem(itemId)));
			when(state.is("not joined"));
			
			oneOf(collector).addSniper(with(aSniperForItem(itemId)));
			when(state.is("not joined"));
			
			oneOf(auction).join();
			then(state.is("joined"));
		}});
		launcher.joinAuction(new Item(itemId, Integer.MAX_VALUE));
	}
	
	private Matcher<AuctionSniper> aSniperForItem(String itemId) {
		return new FeatureMatcher<AuctionSniper, String>(equalTo(itemId), "A sniper for item id", "item") {
			@Override
			protected String featureValueOf(AuctionSniper sniper) {
				return sniper.getSnapshot().itemId;
			}
		};
	}
}
