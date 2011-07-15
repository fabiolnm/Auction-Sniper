package auctionsniper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class SniperPortfolioTest {
	private Mockery context = new Mockery();
	private SniperPortfolio.Listener listener = context.mock(SniperPortfolio.Listener.class);
	private AuctionSniper sniper = new AuctionSniper("item id", null);
	
	@Test
	public void listenerIsNotifiedWhenNewAuctionIsAddedToPortifolio() {
		context.checking(new Expectations() {{
			oneOf(listener).sniperAdded(sniper);
		}});
		
		SniperPortfolio portfolio = new SniperPortfolio();
		portfolio.addListener(listener);
		portfolio.addSniper(sniper);
	}
}
