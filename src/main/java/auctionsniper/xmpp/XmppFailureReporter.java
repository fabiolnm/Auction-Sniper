package auctionsniper.xmpp;

public interface XmppFailureReporter {
	void cannotTranslateMessage(String sniperId, String message, Exception exception);
}
