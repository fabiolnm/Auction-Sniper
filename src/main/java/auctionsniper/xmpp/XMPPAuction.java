package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;

public class XMPPAuction implements Auction {
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %s;";

	private final Chat chat;
	private final AuctionMessageTranslator msgTranslator;

	public XMPPAuction(XMPPConnection connection, String itemId) {
		msgTranslator = new AuctionMessageTranslator(connection.getUser());
		chat = connection.getChatManager().createChat(auctionId(itemId, connection.getServiceName()), msgTranslator);
	}

	private String auctionId(String itemId, String serviceName) {
		return String.format(XmppAuctionHouse.AUCTION_ID_FORMAT, itemId, serviceName);
	}

	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}

	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	private void sendMessage(final String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void setAuctionEventListener(AuctionEventListener listener) {
		msgTranslator.setListener(listener);
	}
}