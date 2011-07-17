package auctionsniper.xmpp;

public class XmppAuctionException extends Exception {
	public XmppAuctionException(String msg) {
		super(msg);
	}

	public XmppAuctionException(Exception exception) {
		super(exception);
	}
}
