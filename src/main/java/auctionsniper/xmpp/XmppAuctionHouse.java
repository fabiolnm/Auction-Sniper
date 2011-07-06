package auctionsniper.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.Main;

public class XmppAuctionHouse {
	private final XMPPConnection connection;

	private XmppAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
	}
	
	public static XmppAuctionHouse connectTo(String hostname, String username, String password)
	throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, Main.AUCTION_RESOURCE);
		return new XmppAuctionHouse(connection);
	}

	public void disconnect() {
		connection.disconnect();
	}

	public Auction auctionFor(String itemId) {
		return new XMPPAuction(connection, itemId);
	}
}
