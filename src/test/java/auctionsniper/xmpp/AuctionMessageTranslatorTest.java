package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import endtoend.ApplicationRunner;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {
	public static final Chat UNUSED_CHAT = null;
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
	private final XmppFailureReporter failureReporter = context.mock(XmppFailureReporter.class);
	private final AuctionMessageTranslator translator = 
		new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, failureReporter);

	@Before
	public void setListener() {
		translator.setListener(listener);
	}
	
	private Message message(String body) {
		Message message = new Message();
		message.setBody(body);
		return message;
	}
	
	@Test
	public void notifiesAuctionClosedWhenCloseMessageReceived() {
		context.checking(new Expectations() {{
			oneOf(listener).auctionClosed();
		}});
		translator.processMessage(UNUSED_CHAT, message("SOLVersion: 1.1; Event: CLOSE;"));
	}
	
	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		context.checking(new Expectations() {{
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
		}});
		String priceMessage = 
			"SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;";
		translator.processMessage(UNUSED_CHAT, message(priceMessage));
	}
	
	@Test 
	public void	notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
		context.checking(new Expectations() {{
			exactly(1).of(listener).currentPrice(234, 5, PriceSource.FromSniper);
		}});
		String sniperBidMessage = 
			"SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: %s;";
		translator.processMessage(UNUSED_CHAT, 
				message(String.format(sniperBidMessage, ApplicationRunner.SNIPER_ID)));
	}
	
	@Test
	public void notifiesAuctionFailedWhenBadMessageReceived() {
		String badMessage = "a bad message";
		expectFailureAndLogMessage(badMessage);
		translator.processMessage(UNUSED_CHAT, message(badMessage));
	}

	@Test
	public void notifiesAuctionFailedWhenEventTypeMissing() {
		String aMessageWithTypeMissing = 
			"SOLVersion: 1.1; CurrentPrice: 234; Increment: 5; Bidder: " + ApplicationRunner.SNIPER_ID + ";";
		expectFailureAndLogMessage(aMessageWithTypeMissing);
		translator.processMessage(UNUSED_CHAT, message(aMessageWithTypeMissing));
	}
	
	private void expectFailureAndLogMessage(final String badMessage) {
		context.checking(new Expectations() {{
			exactly(1).of(listener).auctionFailed();
			exactly(1).of(failureReporter).cannotTranslateMessage(
					with(ApplicationRunner.SNIPER_ID), 
					with(badMessage), with(any(Exception.class)));
		}});
	}
}