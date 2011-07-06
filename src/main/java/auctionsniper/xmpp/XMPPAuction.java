package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;

public class XMPPAuction implements Auction {
	private final Chat chat;
	private final AuctionMessageTranslator msgTranslator;

	public XMPPAuction(XMPPConnection connection, String itemId) {
		msgTranslator = new AuctionMessageTranslator(connection.getUser());
		chat = connection.getChatManager().createChat(auctionId(itemId, connection.getServiceName()), msgTranslator);
	}

	private String auctionId(String itemId, String serviceName) {
		return String.format(Main.AUCTION_ID_FORMAT, itemId, serviceName);
	}

	public void bid(int amount) {
		sendMessage(String.format(Main.BID_COMMAND_FORMAT, amount));
	}

	public void join() {
		sendMessage(Main.JOIN_COMMAND_FORMAT);
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