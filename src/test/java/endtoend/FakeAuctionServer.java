package endtoend;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import auctionsniper.xmpp.XMPPAuction;
import auctionsniper.xmpp.XmppAuctionHouse;

public class FakeAuctionServer {
	public static final String PRICE_FORMAT_MESSAGE = 
		"SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
	public static final String CLOSE_FORMAT_MESSAGE = "SOLVersion: 1.1; Event: CLOSE;";

	public static final String XMPP_HOSTNAME = "localhost";
	private static final String AUCTION_PASSWORD = "auction";
	
	public final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;

	private final SingleMessageListener messageListener = new SingleMessageListener();

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(XmppAuctionHouse.ITEM_ID_AS_LOGIN, itemId), 
				AUCTION_PASSWORD, XmppAuctionHouse.AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			public void chatCreated(Chat chat, boolean createdLocally) {
				currentChat = chat;
				currentChat.addMessageListener(messageListener);
			}
		});
	}

	public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
		messageListener.receivesAMessage(equalTo(XMPPAuction.JOIN_COMMAND_FORMAT));
		assertThat(currentChat.getParticipant(), equalTo(sniperId));
	}	

	public void hasReceivedBidFrom(int bid, String sniperId) throws InterruptedException {
		assertThat(currentChat.getParticipant(), equalTo(sniperId));
		String bidMessage = String.format(XMPPAuction.BID_COMMAND_FORMAT, bid);
		messageListener.receivesAMessage(equalTo(bidMessage));
	}

	public void reportPrice(int price, int increment, String bidder) throws XMPPException {
		currentChat.sendMessage(String.format(PRICE_FORMAT_MESSAGE, price, increment, bidder));
	}

	public void sendInvalidMessageContaining(String brokenMessage) throws XMPPException {
		currentChat.sendMessage(brokenMessage);
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(CLOSE_FORMAT_MESSAGE);
	}

	public void stop() {
		connection.disconnect();
	}
	
	class SingleMessageListener implements MessageListener {
		private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

		public void processMessage(Chat chat, Message message) {
			messages.add(message);
		}

		public void receivesAMessage(Matcher<String> messageMatcher) throws InterruptedException {
			Message message = messages.poll(5, TimeUnit.SECONDS);
			assertThat(message, is(notNullValue()));
			assertThat(message.getBody(), messageMatcher);
		}
	}
}