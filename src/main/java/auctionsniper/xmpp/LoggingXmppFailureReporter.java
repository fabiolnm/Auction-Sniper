package auctionsniper.xmpp;

import java.util.logging.Logger;

public class LoggingXmppFailureReporter implements XmppFailureReporter {
	private final Logger logger;
	
	public final static String COULD_NOT_TRANSLATE_MESSAGE_FORMAT = 
		"[%s] Could not translate message [%s] due to %s";

	public LoggingXmppFailureReporter(Logger logger) {
		this.logger = logger;
	}

	public void cannotTranslateMessage(String sniperId, String message, Exception exception) {
		logger.severe(String.format(COULD_NOT_TRANSLATE_MESSAGE_FORMAT, sniperId, message, exception));
	}
}
